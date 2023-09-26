package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataLiteDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.mongo.MongoCustomerRepository
import com.ajaxproject.warehouse.repository.mongo.MongoProductRepository
import com.ajaxproject.warehouse.repository.mongo.MongoWaybillRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
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

    fun MongoWaybill.mapToLiteDto(): MongoWaybillDataLiteDto {
        val totalPrice = products.asSequence()
            .map { mongoProductRepository.getById(it.productId) to it.amount }
            .map { (product, amount) -> (product?.price ?: 0.0) * amount}
            .sum()

        return MongoWaybillDataLiteDto(
            id = id.toString(),
            customerId = customerId.toString(),
            date = date,
            totalPrice = totalPrice
        )
    }

    fun MongoWaybill.mapToDataDto(): MongoWaybillDataDto {
        val productList: List<MongoWaybillDataDto.MongoWaybillProductDataDto> = products.asSequence()
            .map { mongoProductRepository.getById(it.productId) to it.amount }
            .map { (product, amount) -> MongoWaybillDataDto.MongoWaybillProductDataDto( // TODO
                id = product?.id.toString(),
                title = product?.title as String,
                price = product.price,
                orderedAmount = amount
            ) }
            .toList()
        val customer: MongoCustomer = mongoCustomerRepository.getById(customerId)
            ?: throw NotFoundException("Customer with id $customerId not found")
        return MongoWaybillDataDto(
            id = id.toString(),
            date = date,
            customer = customer.mapToLiteDto(),
            products = productList
        )
    }

    fun MongoCustomer.mapToLiteDto() = MongoCustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoWaybillCreateDto.mapToEntity() =
        MongoWaybill(
            customerId = ObjectId(customerId),
            date = date,
            products = products?.map{ it.mapToWaybillProduct() } ?: listOf()
        )

    fun MongoWaybillCreateDto.MongoWaybillProductCreateDto.mapToWaybillProduct() =
        MongoWaybill.MongoWaybillProduct(
            productId = ObjectId(productId),
            amount = amount
        )

}
