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
import com.ajaxproject.warehouse.repository.MongoCustomerRepository
import com.ajaxproject.warehouse.repository.MongoProductRepository
import com.ajaxproject.warehouse.repository.MongoWaybillRepository
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

    override fun findAllWaybills(): List<WaybillDataLiteDto> =
        mongoWaybillRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): WaybillDataDto {
        val waybill: MongoWaybill = mongoWaybillRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Waybill with id $id not found")
        return waybill.mapToDataDto()
    }

    override fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: String): WaybillDataDto {
        require(infoUpdateDto.id == id) { "Mapping id is not equal to request body id" }
        var mongoWaybill: MongoWaybill = mongoWaybillRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Waybill with id $id not found")
        mongoCustomerRepository.getById(ObjectId(infoUpdateDto.customerId))
            ?: throw NotFoundException("Customer with id ${infoUpdateDto.customerId} not found")
        mongoWaybill = mongoWaybillRepository.save(mongoWaybill.setUpdatedData(infoUpdateDto))
        return mongoWaybill.mapToDataDto()
    }

    override fun createWaybill(createDto: WaybillCreateDto): WaybillDataDto {
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
        val productList: List<WaybillDataDto.MongoWaybillProductDataDto> = getListOfProducts()
        val customer: MongoCustomer = mongoCustomerRepository.getById(customerId)
            ?: throw InternalEntityNotFoundException("Customer with id $customerId not found")
        return WaybillDataDto(
            id = id.toString(),
            date = date,
            customer = customer.mapToLiteDto(),
            products = productList,
            totalPrice = productList.sumOf { it.price * it.orderedAmount }
        )
    }

    fun MongoWaybill.getListOfProducts(): List<WaybillDataDto.MongoWaybillProductDataDto> = products.asSequence()
        .map {
            val product = mongoProductRepository.getById(it.productId) // TODO ask if I should throw exception
            product?.mapToWaybillProductDataDto(it.amount)       // TODO or just insert
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
        WaybillDataDto.MongoWaybillProductDataDto(
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

    fun WaybillCreateDto.MongoWaybillProductCreateDto.mapToWaybillProduct() =
        MongoWaybill.MongoWaybillProduct(
            productId = ObjectId(productId),
            amount = amount as Int
        )

}