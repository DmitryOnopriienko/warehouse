package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.entity.Customer

interface CustomerService {
    fun findAllCustomers(): List<Customer>
    fun findById(id: Int): Customer
}
