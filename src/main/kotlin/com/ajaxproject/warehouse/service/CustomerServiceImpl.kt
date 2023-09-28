package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.MongoCustomerRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    val mongoCustomerRepository: MongoCustomerRepository
) : CustomerService {

    override fun findAllCustomers(): List<CustomerDataLiteDto> =
        mongoCustomerRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): CustomerDataDto {
        val mongoCustomer: MongoCustomer = mongoCustomerRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Customer with id $id not found")
        return mongoCustomer.mapToDataDto()
    }

    override fun createCustomer(createDto: CustomerCreateDto): CustomerDataDto {
        val customer: MongoCustomer =
            mongoCustomerRepository.createCustomer(createDto.mapToEntity())
        return customer.mapToDataDto()
    }

    override fun updateCustomer(updateDto: CustomerUpdateDto, id: String): CustomerDataDto {
        var customer: MongoCustomer = mongoCustomerRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Customer with id $id not found")
        customer = customer.setUpdatedData(updateDto)
        mongoCustomerRepository.save(customer)
        return customer.mapToDataDto()
    }

    override fun deleteById(id: String) {
        mongoCustomerRepository.deleteById(ObjectId(id))
    }

    fun MongoCustomer.mapToLiteDto(): CustomerDataLiteDto = CustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoCustomer.mapToDataDto(): CustomerDataDto {
        val waybills: List<MongoWaybill> = mongoCustomerRepository.findCustomerWaybills(id as ObjectId)
        return CustomerDataDto(
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

    fun MongoCustomer.setUpdatedData(updateDto: CustomerUpdateDto): MongoCustomer {
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
