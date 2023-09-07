package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductsController(val productService: ProductService) {

    @GetMapping
    fun findAllProducts() = productService.findAllProducts() // TODO fix Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: "products"

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int) = productService.findById(id)
}
