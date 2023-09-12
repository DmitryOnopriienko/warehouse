package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto

interface CustomerService {
    fun findAllCustomers(): List<CustomerDataLiteDto>
    fun findById(id: Int): CustomerDataDto
    fun createCustomer(createDto: CustomerCreateDto): CustomerDataDto
    fun deleteById(id: Int)
    fun updateCustomer(updateDto: CustomerUpdateDto, id: Int): CustomerDataDto
}
