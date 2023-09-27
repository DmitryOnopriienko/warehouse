package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductUpdateDto
import com.ajaxproject.warehouse.service.ProductServiceMongo
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
class ProductsController(
    val productServiceMongo: ProductServiceMongo
) {

    @GetMapping
    fun findAllProductsMongo(): List<MongoProductDataLiteDto> = productServiceMongo.findAllProducts()

    @GetMapping("/{id}")
    fun findByIdMongo(@PathVariable id: String): MongoProductDataDto =
        productServiceMongo.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProductMongo(@RequestBody @Valid createDto: ProductCreateDto): MongoProductDataDto =
        productServiceMongo.createProduct(createDto)

    @PutMapping("/{id}")
    fun updateProductMongo(
        @RequestBody @Valid updateDto: MongoProductUpdateDto,
        @PathVariable id: String
    ): MongoProductDataDto = productServiceMongo.updateProduct(updateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProductMongo(@PathVariable id: String): Unit = productServiceMongo.deleteById(id)
}
