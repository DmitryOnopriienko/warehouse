package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto

interface CustomerService {
    fun findAllCustomers(): List<CustomerDataLiteDto>
    fun findById(id: Int): CustomerDataDto
    fun createCustomer(createDto: CustomerCreateDto)
}
