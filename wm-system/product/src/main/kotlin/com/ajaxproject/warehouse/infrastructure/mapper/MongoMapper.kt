package com.ajaxproject.warehouse.infrastructure.mapper

import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.adapter.mongo.entity.MongoProduct
import org.bson.types.ObjectId

fun MongoProduct.mapToDomain(): Product = Product(
    id = this.id?.toString(),
    title = this.title,
    price = this.price,
    amount = this.amount,
    about = this.about
)

fun Product.mapToMongo(): MongoProduct = MongoProduct(
    id = this.id?.let { ObjectId(it) },
    title = this.title,
    price = this.price,
    amount = this.amount,
    about = this.about
)
