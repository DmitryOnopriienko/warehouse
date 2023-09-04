package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("/products")
class ProductsController(val productService: ProductService) {

    @GetMapping
    fun findAll() = productService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int) = productService.findById(id)
}
