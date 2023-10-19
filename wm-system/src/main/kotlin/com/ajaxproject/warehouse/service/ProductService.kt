package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductSaveDto
import jakarta.validation.Valid
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductService {

    fun findAllProducts(): Flux<ProductDataLiteDto>

    fun getById(id: String): Mono<ProductDataDto>

    fun createProduct(@Valid createDto: ProductSaveDto): Mono<ProductDataDto>

    fun updateProduct(@Valid updateDto: ProductSaveDto, id: String): Mono<ProductDataDto>

    fun deleteById(id: String): Mono<Unit>
}
