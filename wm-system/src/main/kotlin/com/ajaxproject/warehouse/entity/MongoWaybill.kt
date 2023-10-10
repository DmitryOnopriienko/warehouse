package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(MongoWaybill.COLLECTION_NAME)
data class MongoWaybill(
    @Id
    val id: ObjectId? = null,
    val customerId: ObjectId,
    val date: LocalDate,
    val products: List<MongoWaybillProduct> = emptyList()
) {
    data class MongoWaybillProduct(
        val productId: ObjectId,
        val amount: Int
    )

    companion object {
        const val COLLECTION_NAME = "waybill"
    }
}
