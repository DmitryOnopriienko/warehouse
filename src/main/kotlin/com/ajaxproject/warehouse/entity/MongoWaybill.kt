package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document("waybill")
data class MongoWaybill(
    @Id
    val id: ObjectId? = null,
    val date: LocalDate,

    @Field("customer_id")
    val customerId: ObjectId,

    @Field("product_ids")
    val productIds: List<ObjectId> = mutableListOf()
)
