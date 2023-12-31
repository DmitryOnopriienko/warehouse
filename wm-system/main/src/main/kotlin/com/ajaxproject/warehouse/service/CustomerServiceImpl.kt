package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.entity.MongoCustomer
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

    override fun findAllCustomers(): Flux<CustomerDataLiteDto> =
        customerRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): Mono<CustomerDataDto> =
        customerRepository.findById(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Customer with id $id not found")) }
            .flatMap { it.mapToDataDto() }

    override fun createCustomer(createDto: CustomerCreateDto): Mono<CustomerDataDto> {
        val customer: Mono<MongoCustomer> = customerRepository.createCustomer(createDto.mapToEntity())
        return customer.flatMap { it.mapToDataDto() }
    }

    @Transactional
    override fun updateCustomer(updateDto: CustomerUpdateDto, id: String): Mono<CustomerDataDto> =
        customerRepository.findById(ObjectId(id))
            .switchIfEmpty { Mono.error(NotFoundException("Customer with id $id not found")) }
            .map { it.setUpdatedData(updateDto) }
            .flatMap {
                customerRepository.save(it)
            }
            .flatMap { it.mapToDataDto() }

    override fun deleteById(id: String): Mono<Unit> =
        customerRepository.deleteById(ObjectId(id))

    fun MongoCustomer.mapToLiteDto(): CustomerDataLiteDto = CustomerDataLiteDto(
        id = id.toString(),
        firstName = firstName,
        surname = surname,
        patronymic = patronymic,
        email = email,
        phoneNumber = phoneNumber
    )

    fun MongoCustomer.mapToDataDto(): Mono<CustomerDataDto> {
        return customerRepository.findCustomerWaybills(id as ObjectId)
            .collectList()
            .map {
                CustomerDataDto(
                    id = id.toString(),
                    firstName = firstName,
                    surname = surname,
                    patronymic = patronymic,
                    email = email,
                    phoneNumber = phoneNumber,
                    birthday = birthday,
                    comment = comment,
                    waybills = it
                )
            }
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
