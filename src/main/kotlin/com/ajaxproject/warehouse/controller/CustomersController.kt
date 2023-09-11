package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.service.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomersController(val customerService: CustomerService) {

    @GetMapping
    fun findAllCustomers(): List<CustomerDataLiteDto> = customerService.findAllCustomers()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): CustomerDataDto = customerService.findById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody @Valid createDto: CustomerCreateDto): CustomerDataDto =
        customerService.createCustomer(createDto)
}
