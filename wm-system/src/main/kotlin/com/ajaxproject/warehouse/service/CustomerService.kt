package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerService {

    fun deleteById(id: String): Mono<Unit>

    fun updateCustomer(updateDto: CustomerUpdateDto, id: String): Mono<CustomerDataDto>

    fun createCustomer(createDto: CustomerCreateDto): Mono<CustomerDataDto>

    fun getById(id: String): Mono<CustomerDataDto>

    fun findAllCustomers(): Flux<CustomerDataLiteDto>
}
