package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductsController(val productService: ProductService) {

    @GetMapping
    fun findAllProducts(): List<ProductDataLiteDto> = productService.findAllProducts()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ProductDataDto = productService.findById(id)
}
