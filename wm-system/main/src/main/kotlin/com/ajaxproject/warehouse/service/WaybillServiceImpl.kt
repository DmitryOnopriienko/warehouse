package com.ajaxproject.warehouse.service

import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillCreatedEvent
import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillUpdatedEvent
import com.ajaxproject.warehouse.application.port.ProductRepositoryOutPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.InternalEntityNotFoundException
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.CustomerRepository
import com.ajaxproject.warehouse.repository.WaybillRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDate
import java.time.ZoneOffset

@Service
@Suppress("TooManyFunctions")
class WaybillServiceImpl(
    val waybillRepository: WaybillRepository,
    val productRepository: ProductRepositoryOutPort,
    val customerRepository: CustomerRepository,
    val kafkaProducerService: KafkaProducerService
) : WaybillService {

    override fun findAllWaybills(): Flux<WaybillDataLiteDto> =
        waybillRepository.findAll().flatMap { it.mapToLiteDto() }

    override fun getById(id: String): Mono<WaybillDataDto> =
        waybillRepository.findById(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Waybill with id $id not found")) }
            .flatMap { it.mapToDataDto() }

    @Transactional
    override fun createWaybill(createDto: WaybillCreateDto): Mono<WaybillDataDto> =
        customerRepository.findById(ObjectId(createDto.customerId))
            .switchIfEmpty { Mono.error(NotFoundException("Customer with id ${createDto.customerId} not found")) }
            .flatMap {
                val productIds = createDto.products.map { ObjectId(it.productId) }
                findValidEntitiesOrError(productIds)
            }
            .then(createWaybillAndMapToDto(createDto))
            .flatMap { waybillDto ->
                kafkaProducerService.sendWaybillCreationEvent(waybillDto.mapToCreatedEventProto())
                    .thenReturn(waybillDto)
            }

    @Transactional
    override fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: String): Mono<WaybillDataDto> =
        waybillRepository.findById(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Waybill with id $id not found")) }
            .flatMap { waybill ->
                customerRepository.findById(ObjectId(infoUpdateDto.customerId))
                    .switchIfEmpty {
                        Mono.error(NotFoundException("Customer with id ${infoUpdateDto.customerId} not found"))
                    }.thenReturn(waybill)
            }
            .flatMap { waybillRepository.save(it.setUpdatedData(infoUpdateDto)) }
            .flatMap { mongoWaybill ->
                mongoWaybill.mapToDataDto().flatMap { waybillDataDto ->
                    kafkaProducerService.sendWaybillUpdatedEvent(
                        WaybillUpdatedEvent.newBuilder().also { event ->
                            event.waybill = waybillDataDto.mapToProto()
                        }.build()
                    ).thenReturn(waybillDataDto)
                }
            }

    override fun deleteById(id: String): Mono<Unit> =
        waybillRepository.deleteById(ObjectId(id))

    private fun MongoWaybill.getListOfProducts(): Mono<List<WaybillDataDto.WaybillProductDataDto>> =
        findValidEntitiesOrError(products.map { it.productId })
            .flatMap { validProducts ->
                products.mapNotNull { waybillProduct ->
                    val validProduct = validProducts.find { it.id == waybillProduct.productId.toString() }
                    validProduct?.mapToWaybillProductDataDto(waybillProduct.amount)
                }.toMono()
            }

    private fun findValidEntitiesOrError(productIds: List<ObjectId>): Mono<List<Product>> =
        productRepository.findValidEntities(productIds.map { it.toString() })
            .collectList()
            .handle { validProducts, sink ->
                val invalidIds: Collection<String> = productIds
                    .map { it.toString() }
                    .toMutableSet()
                    .apply {
                        removeAll(validProducts.asSequence().mapNotNull { it.id }.toSet())
                    }
                if (invalidIds.isEmpty()) {
                    sink.next(validProducts)
                } else {
                    val errorList: List<String> = invalidIds.map { "Product with id $it not found" }
                    sink.error(InternalEntityNotFoundException(errorList))
                }
            }

    private fun createWaybillAndMapToDto(createDto: WaybillCreateDto): Mono<WaybillDataDto> =
        waybillRepository
            .createWaybill(createDto.mapToEntity())
            .flatMap { it.mapToDataDto() }

    private fun MongoWaybill.mapToLiteDto(): Mono<WaybillDataLiteDto> =
        getListOfProducts()
            .map {
                WaybillDataLiteDto(
                    id = id.toString(),
                    customerId = customerId.toString(),
                    date = date,
                    totalPrice = it.sumOf { it.price * it.orderedAmount }
                )
            }

    private fun MongoWaybill.mapToDataDto(): Mono<WaybillDataDto> =
        getListOfProducts()
            .flatMap { productList ->
                customerRepository.findById(customerId)
                    .switchIfEmpty {
                        Mono.error(InternalEntityNotFoundException("Customer with id $customerId not found"))
                    }.map {
                        WaybillDataDto(
                            id = id.toString(),
                            date = date,
                            customer = it.mapToLiteDto(),
                            products = productList,
                            totalPrice = productList.sumOf { it.price * it.orderedAmount }
                        )
                    }
            }

    private fun WaybillDataDto.mapToCreatedEventProto(): WaybillCreatedEvent =
        WaybillCreatedEvent.newBuilder().also { event ->
            event.waybillBuilder.also { waybill ->
                waybill.id = id
                waybill.customerId = customer.id
                waybill.dateBuilder.seconds = date.atStartOfDay().atOffset(ZoneOffset.UTC).toEpochSecond()
            }
        }.build()

    private fun MongoCustomer.mapToLiteDto() = CustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    private fun MongoWaybill.setUpdatedData(infoUpdateDto: WaybillInfoUpdateDto): MongoWaybill =
        this.copy(
            customerId = ObjectId(infoUpdateDto.customerId),
            date = infoUpdateDto.date as LocalDate
        )

    private fun Product.mapToWaybillProductDataDto(amount: Int) =
        WaybillDataDto.WaybillProductDataDto(
            id = id.toString(),
            title = title,
            price = price,
            orderedAmount = amount
        )

    private fun WaybillCreateDto.mapToEntity() =
        MongoWaybill(
            customerId = ObjectId(customerId),
            date = date as LocalDate,
            products = products.map { it.mapToWaybillProduct() }
        )

    private fun WaybillCreateDto.WaybillProductCreateDto.mapToWaybillProduct() =
        MongoWaybill.MongoWaybillProduct(
            productId = ObjectId(productId),
            amount = amount as Int
        )
}
