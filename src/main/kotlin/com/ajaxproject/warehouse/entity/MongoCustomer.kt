package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document("customer")
data class MongoCustomer(
    @Id
    val id: ObjectId? = null,

    @Field(name = "first_name")
    val firstName: String,
    val surname: String,
    val patronymic: String? = null,
    val email: String,

    @Field(name = "phone_number")
    val phoneNumber: String? = null,
    val birthday: LocalDate? = null,
    val comment: String? = null
)
