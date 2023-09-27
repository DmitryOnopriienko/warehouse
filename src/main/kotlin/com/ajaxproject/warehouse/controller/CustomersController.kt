package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.service.CustomerServiceMongo
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomersController(
    val customerServiceMongo: CustomerServiceMongo
) {

    @GetMapping
    fun findAllMongo(): List<CustomerDataLiteDto> = customerServiceMongo.findAllCustomers()

    @GetMapping("/{id}")
    fun findByIdMongo(@PathVariable id: String): CustomerDataDto =
        customerServiceMongo.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomerMongo(
        @RequestBody @Valid createDto: CustomerCreateDto
    ): CustomerDataDto = customerServiceMongo.createCustomer(createDto)

    @PutMapping("/{id}")
    fun updateCustomerMongo(
        @PathVariable id: String,
        @RequestBody @Valid updateDto: CustomerUpdateDto
    ): CustomerDataDto = customerServiceMongo.updateCustomer(updateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomerMongo(@PathVariable id: String): Unit = customerServiceMongo.deleteById(id)
}
