package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataLiteDto

interface CustomerServiceMongo {
    fun findAllCustomers(): List<MongoCustomerDataLiteDto>
    fun getById(id: String): MongoCustomerDataDto
    fun createCustomer(createDto: CustomerCreateDto): MongoCustomerDataDto
    fun deleteById(id: String)
}
