package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoCustomerDataLiteDto
import com.ajaxproject.warehouse.service.CustomerService
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
    val customerService: CustomerService,
    val customerServiceMongo: CustomerServiceMongo
) {

    @GetMapping
    fun findAllCustomers(): List<CustomerDataLiteDto> = customerService.findAllCustomers()

    @GetMapping("/mongo")
    fun findAllMongo(): List<MongoCustomerDataLiteDto> = customerServiceMongo.findAllCustomers()

    @GetMapping("/mongo/{id}")
    fun findByIdMongo(@PathVariable id: String): MongoCustomerDataDto = customerServiceMongo.getById(id)

    @PostMapping("/mongo/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomerMongo(
        @RequestBody @Valid createDto: CustomerCreateDto
    ): MongoCustomerDataDto = customerServiceMongo.createCustomer(createDto)

    @DeleteMapping("/mongo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomerMongo(@PathVariable id: String): Unit = customerServiceMongo.deleteById(id)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): CustomerDataDto = customerService.findById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody @Valid createDto: CustomerCreateDto): CustomerDataDto =
        customerService.createCustomer(createDto)

    @PutMapping("/{id}")
    fun updateCustomer(
        @RequestBody @Valid updateDto: CustomerUpdateDto,
        @PathVariable id: Int
    ): CustomerDataDto = customerService.updateCustomer(updateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int): Unit = customerService.deleteById(id)
}
