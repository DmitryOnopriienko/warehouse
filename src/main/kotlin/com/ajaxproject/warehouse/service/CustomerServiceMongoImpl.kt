package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.MongoCustomerRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CustomerServiceMongoImpl(
    val mongoCustomerRepository: MongoCustomerRepository
) : CustomerServiceMongo {

    override fun findAllCustomers(): List<MongoCustomerDataLiteDto> =
        mongoCustomerRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): MongoCustomerDataDto {
        val mongoCustomer: MongoCustomer = mongoCustomerRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Customer with id $id not found")
        return mongoCustomer.mapToDataDto()
    }

    override fun createCustomer(createDto: CustomerCreateDto): MongoCustomerDataDto {
        val customer: MongoCustomer =
            mongoCustomerRepository.createCustomer(createDto.mapToEntity())
        return customer.mapToDataDto()
    }

    override fun updateCustomer(updateDto: MongoCustomerUpdateDto, id: String): MongoCustomerDataDto {
        require(id == updateDto.id) { "Mapping id is not equal to request body id" }
        var customer: MongoCustomer = mongoCustomerRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Customer with id $id not found")
        customer = customer.setUpdatedData(updateDto)
        mongoCustomerRepository.save(customer)
        return customer.mapToDataDto()
    }

    override fun deleteById(id: String) {
        mongoCustomerRepository.deleteById(ObjectId(id))
    }

    fun MongoCustomer.mapToLiteDto(): MongoCustomerDataLiteDto = MongoCustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoCustomer.mapToDataDto(): MongoCustomerDataDto {
        val waybills: List<MongoWaybill> = mongoCustomerRepository.findCustomerWaybills(id)
        return MongoCustomerDataDto(
            id = id.toString(),
            firstName = firstName,
            surname = surname,
            patronymic = patronymic,
            email = email,
            phoneNumber = phoneNumber,
            birthday = birthday,
            comment = comment,
            waybills = waybills
        )
    }

    fun CustomerCreateDto.mapToEntity(): MongoCustomer = MongoCustomer(
        firstName = firstName as String,
        surname = surname as String,
        patronymic = patronymic,
        email = email as String,
        phoneNumber = phoneNumber,
        birthday = birthday,
        comment = comment
    )

    fun MongoCustomer.setUpdatedData(updateDto: MongoCustomerUpdateDto): MongoCustomer {
        return this.copy(
            firstName = updateDto.firstName as String,
            surname = updateDto.surname as String,
            patronymic = updateDto.patronymic,
            email = updateDto.email as String,
            phoneNumber = updateDto.phoneNumber,
            birthday = updateDto.birthday,
            comment = updateDto.comment
        )
    }

}
