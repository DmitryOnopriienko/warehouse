package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.entity.Customer
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(val customerRepository: CustomerRepository) : CustomerService {
    override fun findAllCustomers(): List<Customer> {
        return customerRepository.findAll()
    }

    override fun findById(id: Int): Customer {
        return customerRepository.findById(id).orElseThrow { NotFoundException("Customer with id $id not found") }
    }
}
