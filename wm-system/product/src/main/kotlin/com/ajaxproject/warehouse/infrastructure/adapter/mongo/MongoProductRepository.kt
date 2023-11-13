package com.ajaxproject.warehouse.infrastructure.adapter.mongo

import com.ajaxproject.warehouse.application.port.ProductRepositoryOutPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.adapter.mongo.entity.MongoProduct
import com.ajaxproject.warehouse.infrastructure.adapter.mongo.mapper.mapToDomain
import com.ajaxproject.warehouse.infrastructure.adapter.mongo.mapper.mapToMongo
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Primary
class MongoProductRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : ProductRepositoryOutPort {

    override fun findAll(): Flux<Product> =
        reactiveMongoTemplate.findAll<MongoProduct>()
            .map { it.mapToDomain() }

    override fun findById(id: String): Mono<Product> =
        reactiveMongoTemplate.findById<MongoProduct>(id)
            .map { it.mapToDomain() }

    override fun createProduct(product: Product): Mono<Product> =
        reactiveMongoTemplate.insert<MongoProduct>(product.mapToMongo())
            .map { it.mapToDomain() }

    override fun save(product: Product): Mono<Product> =
        reactiveMongoTemplate.save(product.mapToMongo())
            .map { it.mapToDomain() }

    override fun deleteById(id: String): Mono<Unit> =
        reactiveMongoTemplate.remove<MongoProduct>(
            Query(Criteria.where("_id").`is`(id))
        ).thenReturn(Unit)

    override fun findValidEntities(ids: List<String>): Flux<Product> =
        reactiveMongoTemplate.find<MongoProduct>(
            Query(Criteria.where("_id").`in`(ids))
        ).map { it.mapToDomain() }
}
