package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId

interface MongoProductRepository {
    fun findAll(): List<MongoProduct>
    fun findById(id: ObjectId): MongoProduct?
    fun createProduct(mongoProduct: MongoProduct): MongoProduct
    fun save(mongoProduct: MongoProduct): MongoProduct
    fun deleteById(id: ObjectId)
}
