package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillInfoUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.InternalEntityNotFoundException
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.mongo.MongoCustomerRepository
import com.ajaxproject.warehouse.repository.mongo.MongoProductRepository
import com.ajaxproject.warehouse.repository.mongo.MongoWaybillRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
@Suppress("TooManyFunctions")
class WaybillServiceMongoImpl(
    val mongoWaybillRepository: MongoWaybillRepository,
    val mongoProductRepository: MongoProductRepository,
    val mongoCustomerRepository: MongoCustomerRepository
) : WaybillServiceMongo {

    override fun findAllWaybills(): List<MongoWaybillDataLiteDto> =
        mongoWaybillRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): MongoWaybillDataDto {
        val waybill: MongoWaybill = mongoWaybillRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Waybill with id $id not found")
        return waybill.mapToDataDto()
    }

    override fun updateWaybillInfo(infoUpdateDto: MongoWaybillInfoUpdateDto, id: String): MongoWaybillDataDto {
        require(infoUpdateDto.id == id) { "Mapping id is not equal to request body id" }
        var mongoWaybill: MongoWaybill = mongoWaybillRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Waybill with id $id not found")
        mongoCustomerRepository.getById(ObjectId(infoUpdateDto.customerId))
            ?: throw NotFoundException("Customer with id ${infoUpdateDto.customerId} not found")
        mongoWaybill = mongoWaybillRepository.save(mongoWaybill.setUpdatedData(infoUpdateDto))
        return mongoWaybill.mapToDataDto()
    }

    override fun createWaybill(createDto: MongoWaybillCreateDto): MongoWaybillDataDto {
        val errorList: MutableList<String> = mutableListOf()
        mongoCustomerRepository.getById(ObjectId(createDto.customerId))
            ?: errorList.add("Customer with id ${createDto.customerId} not found")
        createDto.products.forEach { mongoProductRepository.getById(ObjectId(it.productId))
            ?: errorList.add("Product with id ${it.productId} not found") }
        if (errorList.isNotEmpty()) throw NotFoundException(errorList)
        val mongoWaybill: MongoWaybill = mongoWaybillRepository.createWaybill(createDto.mapToEntity())
        return mongoWaybill.mapToDataDto()
    }

    override fun deleteById(id: String) {
        mongoWaybillRepository.deleteById(ObjectId(id))
    }

    fun MongoWaybill.mapToLiteDto(): MongoWaybillDataLiteDto {
        val totalPrice = getListOfProducts().sumOf { it.price }

        return MongoWaybillDataLiteDto(
            id = id.toString(),
            customerId = customerId.toString(),
            date = date,
            totalPrice = totalPrice
        )
    }

    fun MongoWaybill.mapToDataDto(): MongoWaybillDataDto {
        val productList: List<MongoWaybillDataDto.MongoWaybillProductDataDto> = getListOfProducts()
        val customer: MongoCustomer = mongoCustomerRepository.getById(customerId)
            ?: throw InternalEntityNotFoundException("Customer with id $customerId not found")
        return MongoWaybillDataDto(
            id = id.toString(),
            date = date,
            customer = customer.mapToLiteDto(),
            products = productList,
            totalPrice = productList.sumOf { it.price }
        )
    }

    fun MongoWaybill.getListOfProducts(): List<MongoWaybillDataDto.MongoWaybillProductDataDto> = products.asSequence()
        .map {
            val product = mongoProductRepository.getById(it.productId) // TODO ask if I should throw exception
            product?.mapToWaybillProductDataDto(it.amount)       // TODO or just insert
        }
        .filterNotNull()
        .toList()

    fun MongoCustomer.mapToLiteDto() = MongoCustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoWaybill.setUpdatedData(infoUpdateDto: MongoWaybillInfoUpdateDto): MongoWaybill =
        this.copy(
            id = ObjectId(infoUpdateDto.id),
            customerId = ObjectId(infoUpdateDto.customerId),
            date = infoUpdateDto.date as LocalDate
        )

    fun MongoProduct.mapToWaybillProductDataDto(amount: Int) =
        MongoWaybillDataDto.MongoWaybillProductDataDto(
            id = id.toString(),
            title = title,
            price = price,
            orderedAmount = amount
        )

    fun MongoWaybillCreateDto.mapToEntity() =
        MongoWaybill(
            customerId = ObjectId(customerId),
            date = date as LocalDate,
            products = products.map { it.mapToWaybillProduct() }
        )

    fun MongoWaybillCreateDto.MongoWaybillProductCreateDto.mapToWaybillProduct() =
        MongoWaybill.MongoWaybillProduct(
            productId = ObjectId(productId),
            amount = amount as Int
        )

}
