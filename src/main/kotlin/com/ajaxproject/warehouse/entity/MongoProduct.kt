package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("product")
data class MongoProduct(
    @Id
    var id: ObjectId? = null,
    var title: String,
    var price: Double,
    var amount: Int,
    var about: String? = null
)
