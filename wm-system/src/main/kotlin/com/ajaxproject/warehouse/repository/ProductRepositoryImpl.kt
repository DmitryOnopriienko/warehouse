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
        reactiveMongoTemplate.findAll<MongoProduct>(MongoProduct.COLLECTION_NAME)

    override fun findById(id: ObjectId): Mono<MongoProduct> =
        reactiveMongoTemplate.findById<MongoProduct>(id, MongoProduct.COLLECTION_NAME)

    override fun createProduct(mongoProduct: MongoProduct): Mono<MongoProduct> =
        reactiveMongoTemplate.insert(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun save(mongoProduct: MongoProduct): Mono<MongoProduct> =
        reactiveMongoTemplate.save(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun deleteById(id: ObjectId): Mono<Unit> =
        reactiveMongoTemplate.remove<MongoProduct>(
            Query(Criteria.where("_id").`is`(id)),
            MongoProduct.COLLECTION_NAME
        ).thenReturn(Unit)

    override fun getValidIds(ids: List<ObjectId>): Flux<String> =
        reactiveMongoTemplate.find<MongoProduct>(
            Query(Criteria.where("_id").`in`(ids)),
            MongoProduct.COLLECTION_NAME
        ).map { it.id.toString() }
}
