package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProductRepositoryImpl(
    val mongoTemplate: MongoTemplate,
    val reactiveMongoTemplate: ReactiveMongoTemplate
) : ProductRepository {

    override fun findAll(): List<MongoProduct> = mongoTemplate.findAll<MongoProduct>(MongoProduct.COLLECTION_NAME)

    override fun findById(id: ObjectId): MongoProduct? =
        mongoTemplate.findById<MongoProduct>(id, MongoProduct.COLLECTION_NAME)

    override fun createProduct(mongoProduct: MongoProduct): MongoProduct =
        mongoTemplate.insert(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun save(mongoProduct: MongoProduct): MongoProduct =
        mongoTemplate.save(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun deleteById(id: ObjectId) {
        mongoTemplate.findAndRemove<MongoProduct>(
            Query(Criteria.where("_id").`is`(id)),
            MongoProduct.COLLECTION_NAME
        )
    }

    override fun getValidIds(ids: List<ObjectId>): List<String> {
        return mongoTemplate.find<MongoProduct>(
            Query(Criteria.where("_id").`in`(ids)),
            MongoProduct.COLLECTION_NAME
        ).map { it.id.toString() }
    }

    override fun findAllR(): Flux<MongoProduct> =
        reactiveMongoTemplate.findAll<MongoProduct>(MongoProduct.COLLECTION_NAME)

    override fun findByIdR(id: ObjectId): Mono<MongoProduct> =
        reactiveMongoTemplate.findById<MongoProduct>(id, MongoProduct.COLLECTION_NAME)

    override fun createProductR(mongoProduct: MongoProduct): Mono<MongoProduct> =
        reactiveMongoTemplate.insert(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun saveR(mongoProduct: MongoProduct): Mono<MongoProduct> =
        reactiveMongoTemplate.save(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun deleteByIdR(id: ObjectId) {
        reactiveMongoTemplate.findAndRemove<MongoProduct>(
            Query(Criteria.where("_id").`is`(id)),
            MongoProduct.COLLECTION_NAME
        )
    }

    override fun getValidIdsR(ids: List<ObjectId>): Flux<String> =
        reactiveMongoTemplate.find<MongoProduct>(
            Query(Criteria.where("_id").`in`(ids)),
            MongoProduct.COLLECTION_NAME
        ).map { it.id.toString() }
}
