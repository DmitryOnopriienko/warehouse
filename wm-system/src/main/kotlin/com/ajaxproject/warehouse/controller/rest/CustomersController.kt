package com.ajaxproject.warehouse.controller.rest

import com.ajaxproject.warehouse.dto.CustomerCreateDto
import com.ajaxproject.warehouse.dto.CustomerDataDto
import com.ajaxproject.warehouse.dto.CustomerDataLiteDto
import com.ajaxproject.warehouse.dto.CustomerUpdateDto
import com.ajaxproject.warehouse.service.CustomerService
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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/customers")
class CustomersController(
    val customerService: CustomerService
) {

    @GetMapping
    fun findAll(): Flux<CustomerDataLiteDto> = customerService.findAllCustomers()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<CustomerDataDto> =
        customerService.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(
        @RequestBody @Valid createDto: CustomerCreateDto
    ): Mono<CustomerDataDto> = customerService.createCustomer(createDto)

    @PutMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: String,
        @RequestBody @Valid updateDto: CustomerUpdateDto
    ): Mono<CustomerDataDto> = customerService.updateCustomer(updateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: String): Mono<Unit> = customerService.deleteById(id)
}
