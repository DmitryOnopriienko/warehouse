package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class MongoProductRepositoryImpl(
    val mongoTemplate: MongoTemplate
) : MongoProductRepository {

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
}
