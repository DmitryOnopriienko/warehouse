package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerUpdateDto
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

    @GetMapping("/mongo")
    fun findAllMongo(): List<MongoCustomerDataLiteDto> = customerServiceMongo.findAllCustomers()

    @GetMapping("/mongo/{id}")
    fun findByIdMongo(@PathVariable id: String): MongoCustomerDataDto =
        customerServiceMongo.getById(id)

    @PostMapping("/mongo/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomerMongo(
        @RequestBody @Valid createDto: CustomerCreateDto
    ): MongoCustomerDataDto = customerServiceMongo.createCustomer(createDto)

    @PutMapping("/mongo/{id}")
    fun updateCustomerMongo(
        @PathVariable id: String,
        @RequestBody @Valid updateDto: MongoCustomerUpdateDto
    ): MongoCustomerDataDto = customerServiceMongo.updateCustomer(updateDto, id)

    @DeleteMapping("/mongo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomerMongo(@PathVariable id: String): Unit = customerServiceMongo.deleteById(id)
}
