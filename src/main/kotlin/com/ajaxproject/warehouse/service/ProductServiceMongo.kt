package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductUpdateDto

interface ProductServiceMongo {
    fun findAllProducts(): List<MongoProductDataLiteDto>
    fun getById(id: String): MongoProductDataDto
    fun createProduct(createDto: ProductCreateDto): MongoProductDataDto
    fun updateProduct(updateDto: MongoProductUpdateDto, id: String): MongoProductDataDto
    fun deleteById(id: String)
}
