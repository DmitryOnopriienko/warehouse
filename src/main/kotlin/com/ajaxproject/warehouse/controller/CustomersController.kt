package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.service.CustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomersController(val customerService: CustomerService) {

    @GetMapping
    fun findAllCustomers() = customerService.findAllCustomers()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int) = customerService.findById(id)
}
