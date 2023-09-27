package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class MongoProductRepositoryImpl(
    val mongoTemplate: MongoTemplate
) : MongoProductRepository {

    private val productType = MongoProduct::class.java

    override fun findAll(): List<MongoProduct> = mongoTemplate.findAll(productType, MongoProduct.COLLECTION_NAME)

    override fun getById(id: ObjectId): MongoProduct? =
        mongoTemplate.findById(id, productType, MongoProduct.COLLECTION_NAME)

    override fun createProduct(mongoProduct: MongoProduct): MongoProduct =
        mongoTemplate.insert(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun save(mongoProduct: MongoProduct): MongoProduct =
        mongoTemplate.save(mongoProduct, MongoProduct.COLLECTION_NAME)

    override fun deleteById(id: ObjectId) {
        mongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(id)),
            productType,
            MongoProduct.COLLECTION_NAME
        )
    }

}
