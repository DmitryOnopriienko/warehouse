package com.ajaxproject.warehouse.application.port

import com.ajaxproject.warehouse.domain.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductRepositoryOutPort {
    fun findAll(): Flux<Product>

    fun findById(id: String): Mono<Product>

    fun createProduct(product: Product): Mono<Product>

    fun save(product: Product): Mono<Product>

    fun deleteById(id: String): Mono<Unit>

    fun findValidEntities(ids: List<String>): Flux<Product>
}
