package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerService {

    fun findAllCustomers(): List<CustomerDataLiteDto>

    fun getById(id: String): CustomerDataDto

    fun createCustomer(createDto: CustomerCreateDto): CustomerDataDto

    fun deleteById(id: String)

    fun updateCustomer(updateDto: CustomerUpdateDto, id: String): CustomerDataDto

    fun deleteByIdR(id: String): Mono<Unit>

    fun updateCustomerR(updateDto: CustomerUpdateDto, id: String): Mono<CustomerDataDto>

    fun createCustomerR(createDto: CustomerCreateDto): Mono<CustomerDataDto>

    fun getByIdR(id: String): Mono<CustomerDataDto>

    fun findAllCustomersR(): Flux<CustomerDataLiteDto>
}
