package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
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
class ProductRepositoryImpl(
    val reactiveMongoTemplate: ReactiveMongoTemplate
) : ProductRepository {

    override fun findAll(): Flux<MongoProduct> =
        reactiveMongoTemplate.findAll<MongoProduct>()

    override fun findById(id: ObjectId): Mono<MongoProduct> =
        reactiveMongoTemplate.findById<MongoProduct>(id)

    override fun createProduct(mongoProduct: MongoProduct): Mono<MongoProduct> =
        reactiveMongoTemplate.insert(mongoProduct)

    override fun save(mongoProduct: MongoProduct): Mono<MongoProduct> =
        reactiveMongoTemplate.save(mongoProduct)

    override fun deleteById(id: ObjectId): Mono<Unit> =
        reactiveMongoTemplate.remove<MongoProduct>(
            Query(Criteria.where("_id").`is`(id))
        ).thenReturn(Unit)

    override fun findValidEntities(ids: List<ObjectId>): Flux<MongoProduct> =
        reactiveMongoTemplate.find<MongoProduct>(
            Query(Criteria.where("_id").`in`(ids))
        )
}
