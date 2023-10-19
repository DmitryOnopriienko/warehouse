package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.InternalEntityNotFoundException
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.CustomerRepository
import com.ajaxproject.warehouse.repository.ProductRepository
import com.ajaxproject.warehouse.repository.WaybillRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDate

@Service
@Suppress("TooManyFunctions")
class WaybillServiceImpl(
    val waybillRepository: WaybillRepository,
    val productRepository: ProductRepository,
    val customerRepository: CustomerRepository
) : WaybillService {

    override fun findAllWaybills(): List<WaybillDataLiteDto> =
        waybillRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): WaybillDataDto {
        val waybill: MongoWaybill = waybillRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Waybill with id $id not found")
        return waybill.mapToDataDto()
    }

    @Transactional
    override fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: String): WaybillDataDto {
        val mongoWaybill: MongoWaybill = waybillRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Waybill with id $id not found")
        customerRepository.findById(ObjectId(infoUpdateDto.customerId))
            ?: throw NotFoundException("Customer with id ${infoUpdateDto.customerId} not found")
        val updatedWaybill = mongoWaybill.setUpdatedData(infoUpdateDto)
        return waybillRepository.save(updatedWaybill).mapToDataDto()
    }

    @Transactional
    override fun createWaybill(createDto: WaybillCreateDto): WaybillDataDto {
        customerRepository.findById(ObjectId(createDto.customerId))
            ?: throw NotFoundException("Customer with id ${createDto.customerId} not found")

        val validIdList = productRepository.getValidIds(createDto.products.map { ObjectId(it.productId) })
        createDto.products.asSequence()
            .map { it.productId as String }
            .minus(validIdList.toSet())
            .map { "Product with id $it not found" }
            .toList()
            .takeIf { it.isEmpty() }
            ?.let { throw NotFoundException(it) }

        return waybillRepository
            .createWaybill(createDto.mapToEntity())
            .mapToDataDto()
    }

    override fun deleteById(id: String) {
        waybillRepository.deleteById(ObjectId(id))
    }

    override fun findAllWaybillsR(): Flux<WaybillDataLiteDto> =
        waybillRepository.findAllR().map { it.mapToLiteDto() }

    override fun getByIdR(id: String): Mono<WaybillDataDto> {   // TODO make kotlin style
        val waybill = waybillRepository.findByIdR(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Waybill with id $id not found")) }
        return waybill.map { it.mapToDataDto() }
    }

    @Transactional
    override fun createWaybillR(createDto: WaybillCreateDto): Mono<WaybillDataDto> {
        return customerRepository.findByIdR(ObjectId(createDto.customerId))
            .switchIfEmpty { Mono.error(NotFoundException("Customer with id ${createDto.customerId} not found")) }
            .flatMap {
                val productIds = createDto.products.map { ObjectId(it.productId) }
                validateProductsIds(productIds, createDto, it)
            }.then(createWaybillAndMapToDto(createDto))
    }

    private fun validateProductsIds(
        productIds: List<ObjectId>,
        createDto: WaybillCreateDto,
        it: MongoCustomer
    ) = productRepository.getValidIdsR(productIds)
        .collectList()
        .handle<List<String>> { validIdList, sink ->
            val notFoundIds = createDto.products
                .asSequence()
                .map { it.productId as String }
                .filter { it !in validIdList }
                .toList()
            if (notFoundIds.isEmpty()) {
                sink.complete()
            } else {
                val errorMessage = notFoundIds.map { "Product with id $it not found" }
                sink.error(NotFoundException(errorMessage))
            }
        }.thenReturn(it)

    private fun createWaybillAndMapToDto(createDto: WaybillCreateDto): Mono<WaybillDataDto> {
        return waybillRepository
            .createWaybillR(createDto.mapToEntity())
            .map { it.mapToDataDto() }
    }

    override fun deleteByIdR(id: String): Mono<Unit> =
        waybillRepository.deleteByIdR(ObjectId(id))

    @Transactional
    override fun updateWaybillInfoR(infoUpdateDto: WaybillInfoUpdateDto, id: String): Mono<WaybillDataDto> {
        return waybillRepository.findByIdR(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Waybill with id $id not found")) }
            .flatMap {
                customerRepository.findByIdR(ObjectId(infoUpdateDto.customerId))
                    .switchIfEmpty {
                        Mono.error(NotFoundException("Customer with id ${infoUpdateDto.customerId} not found"))
                    }.thenReturn(it)
            }.flatMap { mongoWaybill ->
                waybillRepository
                    .saveR(mongoWaybill.setUpdatedData(infoUpdateDto))
                    .map { it.mapToDataDto() }
            }
    }

    fun MongoWaybill.mapToLiteDto(): WaybillDataLiteDto {
        val totalPrice = getListOfProducts().sumOf { it.price * it.orderedAmount }

        return WaybillDataLiteDto(
            id = id.toString(),
            customerId = customerId.toString(),
            date = date,
            totalPrice = totalPrice
        )
    }

    fun MongoWaybill.mapToDataDto(): WaybillDataDto {
        val productList: List<WaybillDataDto.WaybillProductDataDto> = getListOfProducts()
        val customer: MongoCustomer = customerRepository.findById(customerId)
            ?: throw InternalEntityNotFoundException("Customer with id $customerId not found")
        return WaybillDataDto(
            id = id.toString(),
            date = date,
            customer = customer.mapToLiteDto(),
            products = productList,
            totalPrice = productList.sumOf { it.price * it.orderedAmount }
        )
    }

    fun MongoWaybill.getListOfProducts(): List<WaybillDataDto.WaybillProductDataDto> = products.asSequence()
        .map {
            val product = productRepository.findById(it.productId)
            product?.mapToWaybillProductDataDto(it.amount)
        }
        .filterNotNull()
        .toList()

    fun MongoCustomer.mapToLiteDto() = CustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoWaybill.setUpdatedData(infoUpdateDto: WaybillInfoUpdateDto): MongoWaybill =
        this.copy(
            customerId = ObjectId(infoUpdateDto.customerId),
            date = infoUpdateDto.date as LocalDate
        )

    fun MongoProduct.mapToWaybillProductDataDto(amount: Int) =
        WaybillDataDto.WaybillProductDataDto(
            id = id.toString(),
            title = title,
            price = price,
            orderedAmount = amount
        )

    fun WaybillCreateDto.mapToEntity() =
        MongoWaybill(
            customerId = ObjectId(customerId),
            date = date as LocalDate,
            products = products.map { it.mapToWaybillProduct() }
        )

    fun WaybillCreateDto.WaybillProductCreateDto.mapToWaybillProduct() =
        MongoWaybill.MongoWaybillProduct(
            productId = ObjectId(productId),
            amount = amount as Int
        )
}
