package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.annotation.RequestLimit
import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductUpdateDto
import com.ajaxproject.warehouse.service.ProductService
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
@RequestMapping("/products")
class ProductsController(val productService: ProductService) {

    @GetMapping
    fun findAllProducts(): List<ProductDataLiteDto> = productService.findAllProducts()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ProductDataDto = productService.findById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody @Valid createDto: ProductCreateDto): ProductDataDto =
        productService.createProduct(createDto)

    @PutMapping("/{id}")
    fun updateProduct(
        @RequestBody @Valid updateDto: ProductUpdateDto,
        @PathVariable id: Int
    ): ProductDataDto = productService.updateProduct(updateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Int): Unit = productService.deleteById(id)
}
