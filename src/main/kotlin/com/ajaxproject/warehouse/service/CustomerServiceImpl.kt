package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.entity.Customer
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.CustomerRepository
import com.ajaxproject.warehouse.repository.WaybillRepository
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    val customerRepository: CustomerRepository,
    val waybillRepository: WaybillRepository
) : CustomerService {

    override fun findAllCustomers(): List<CustomerDataLiteDto> {
        return customerRepository.findAll()
            .map { it.mapToLiteDto() }
    }

    override fun findById(id: Int): CustomerDataDto {
        return customerRepository.findById(id)
            .orElseThrow { NotFoundException("Customer with id $id not found") }
            .mapToDataDto()
    }

    override fun createCustomer(createDto: CustomerCreateDto): CustomerDataDto {
        val customer = customerRepository.save(createDto.mapToEntity())
        return customer.mapToDataDto()
    }

    override fun updateCustomer(updateDto: CustomerUpdateDto, id: Int): CustomerDataDto {
        require(id == updateDto.id) { "Mapping id is not equal to request body id" }
        val customer: Customer = customerRepository.findById(id)
            .orElseThrow { NotFoundException("Customer with id $id not found") }
        customer.setUpdatedData(updateDto)
        customerRepository.save(customer)
        return customer.mapToDataDto()
    }

    override fun deleteById(id: Int) {
        customerRepository.deleteById(id)
    }

    fun Customer.mapToLiteDto(): CustomerDataLiteDto = CustomerDataLiteDto(
        id = id,
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun Customer.mapToDataDto(): CustomerDataDto = CustomerDataDto(
        id = id,
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber,
        birthday = birthday,
        comment = comment,
        waybills = waybillRepository.findByCustomer(this)
    )

    fun CustomerCreateDto.mapToEntity(): Customer = Customer(
        id = null,
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber,
        birthday = birthday,
        comment = comment
    )

    private fun Customer.setUpdatedData(updateDto: CustomerUpdateDto) {
        firstName = updateDto.firstName as String
        surname = updateDto.surname as String
        patronymic = updateDto.patronymic
        email = updateDto.email as String
        phoneNumber = updateDto.phoneNumber
        birthday = updateDto.birthday
        comment = updateDto.comment
    }
}
