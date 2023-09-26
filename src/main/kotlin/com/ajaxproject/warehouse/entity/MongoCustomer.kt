package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document(MongoCustomer.COLLECTION_NAME)
data class MongoCustomer(
    @Id
    val id: ObjectId? = null,
    val firstName: String,
    val surname: String,
    val patronymic: String? = null,
    val email: String,
    val phoneNumber: String? = null,
    val birthday: LocalDate? = null,
    val comment: String? = null
) {
    companion object {
        const val COLLECTION_NAME = "customer"
    }
}
