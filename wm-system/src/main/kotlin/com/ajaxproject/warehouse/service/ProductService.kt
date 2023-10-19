package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductSaveDto
import jakarta.validation.Valid
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductService {

    fun findAllProducts(): List<ProductDataLiteDto>

    fun getById(id: String): ProductDataDto

    fun createProduct(@Valid createDto: ProductSaveDto): ProductDataDto

    fun updateProduct(@Valid updateDto: ProductSaveDto, id: String): ProductDataDto

    fun deleteById(id: String)

    fun findAllProductsR(): Flux<ProductDataLiteDto>

    fun getByIdR(id: String): Mono<ProductDataDto>

    fun createProductR(@Valid createDto: ProductSaveDto): Mono<ProductDataDto>

    fun updateProductR(@Valid updateDto: ProductSaveDto, id: String): Mono<ProductDataDto>

    fun deleteByIdR(id: String): Mono<Unit>
}
