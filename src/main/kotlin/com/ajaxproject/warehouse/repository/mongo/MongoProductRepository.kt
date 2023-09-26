package com.ajaxproject.warehouse.repository.mongo

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId

interface MongoProductRepository {
    fun findAll(): List<MongoProduct>
    fun getById(id: ObjectId): MongoProduct?
    fun createProduct(mongoProduct: MongoProduct): MongoProduct
    fun save(mongoProduct: MongoProduct): MongoProduct
    fun deleteById(id: ObjectId)
}
