package com.ajaxproject.warehouse.application.port

import com.ajaxproject.warehouse.domain.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductServiceInPort {
    fun findAllProducts(): Flux<Product>

    fun getById(id: String): Mono<Product>

    fun createProduct(productToCreate: Product): Mono<Product>

    fun updateProduct(productToUpdate: Product, id: String): Mono<Product>

    fun deleteById(id: String): Mono<Unit>
}
