package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductRepository {

    fun findAll(): Flux<MongoProduct>

    fun findById(id: ObjectId): Mono<MongoProduct>

    fun createProduct(mongoProduct: MongoProduct): Mono<MongoProduct>

    fun save(mongoProduct: MongoProduct): Mono<MongoProduct>

    fun deleteById(id: ObjectId): Mono<Unit>

    fun getValidIds(ids: List<ObjectId>): Flux<String>
}
