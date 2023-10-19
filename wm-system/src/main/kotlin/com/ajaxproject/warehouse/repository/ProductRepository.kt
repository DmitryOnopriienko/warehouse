package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductRepository {

    fun findAll(): List<MongoProduct>

    fun findAllR(): Flux<MongoProduct>

    fun findById(id: ObjectId): MongoProduct?

    fun findByIdR(id: ObjectId): Mono<MongoProduct>

    fun createProduct(mongoProduct: MongoProduct): MongoProduct

    fun createProductR(mongoProduct: MongoProduct): Mono<MongoProduct>

    fun save(mongoProduct: MongoProduct): MongoProduct

    fun saveR(mongoProduct: MongoProduct): Mono<MongoProduct>

    fun deleteById(id: ObjectId)

    fun deleteByIdR(id: ObjectId): Mono<Unit>

    fun getValidIds(ids: List<ObjectId>): List<String>

    fun getValidIdsR(ids: List<ObjectId>): Flux<String>
}
