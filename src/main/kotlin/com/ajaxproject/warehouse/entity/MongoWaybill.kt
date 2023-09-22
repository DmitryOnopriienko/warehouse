package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document("waybill")
data class MongoWaybill(
    @Id
    var id: ObjectId? = null,
    var date: LocalDate,

    @Field("customer_id")
    var customerId: ObjectId,

    @Field("product_ids")
    var productIds: List<ObjectId> = mutableListOf()
)
