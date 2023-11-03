package com.ajaxproject.warehouse.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(MongoProduct.COLLECTION_NAME)
data class MongoProduct(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)   // TODO add separate model for Redis
    val id: ObjectId? = null,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String? = null
) {
    companion object {
        const val COLLECTION_NAME = "product"
    }
}
