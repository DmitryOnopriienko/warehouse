package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document(MongoWaybill.COLLECTION_NAME)
data class MongoWaybill(
    @Id
    val id: ObjectId? = null,

    @Field("customer_id")
    val customerId: ObjectId,
    val date: LocalDate,
    val products: List<MongoWaybillProduct> = listOf()
) {
    @Document("products")
    data class MongoWaybillProduct(
        @Field("product_id")
        val id: ObjectId,
        val amount: Int
    )

    companion object {
        const val COLLECTION_NAME = "waybill"
    }
}
