package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.CustomerRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class CustomerServiceImpl(
    val customerRepository: CustomerRepository
) : CustomerService {

    override fun findAllCustomers(): List<CustomerDataLiteDto> =
        customerRepository.findAll().map { it.mapToLiteDto() }

    override fun findAllCustomersR(): Flux<CustomerDataLiteDto> =
        customerRepository.findAllR().map { it.mapToLiteDto() }

    override fun getById(id: String): CustomerDataDto {
        val mongoCustomer: MongoCustomer = customerRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Customer with id $id not found")
        return mongoCustomer.mapToDataDto()
    }

    override fun getByIdR(id: String): Mono<CustomerDataDto> {
        val mongoCustomer: Mono<MongoCustomer> = customerRepository.findByIdR(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Customer with id $id not found")) }
        return mongoCustomer.map { it.mapToDataDto() }
    }

    override fun createCustomer(createDto: CustomerCreateDto): CustomerDataDto {
        val customer: MongoCustomer =
            customerRepository.createCustomer(createDto.mapToEntity())
        return customer.mapToDataDto()
    }

    override fun createCustomerR(createDto: CustomerCreateDto): Mono<CustomerDataDto> {
        val customer: Mono<MongoCustomer> = customerRepository.createCustomerR(createDto.mapToEntity())
        return customer.map { it.mapToDataDto() }
    }

    @Transactional
    override fun updateCustomer(updateDto: CustomerUpdateDto, id: String): CustomerDataDto {
        val customer: MongoCustomer = customerRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Customer with id $id not found")
        val updatedCustomer = customer.setUpdatedData(updateDto)
        return customerRepository.save(updatedCustomer).mapToDataDto()
    }

    @Transactional
    override fun updateCustomerR(updateDto: CustomerUpdateDto, id: String): Mono<CustomerDataDto> {
        val customer = customerRepository.findByIdR(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Customer with id $id not found")) }
        return customer
            .map { it.setUpdatedData(updateDto) }
            .flatMap {
                customerRepository.saveR(it)
            }
            .map { it.mapToDataDto() }
    }

    override fun deleteById(id: String) {
        customerRepository.deleteById(ObjectId(id))
    }

    override fun deleteByIdR(id: String): Mono<Unit> =
        customerRepository.deleteByIdR(ObjectId(id))

    fun MongoCustomer.mapToLiteDto(): CustomerDataLiteDto = CustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoCustomer.mapToDataDto(): CustomerDataDto {
        val waybills: List<MongoWaybill> = customerRepository.findCustomerWaybills(id as ObjectId)
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
