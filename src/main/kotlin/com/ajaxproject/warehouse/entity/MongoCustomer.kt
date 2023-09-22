package com.ajaxproject.warehouse.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document("customer")
data class MongoCustomer(
    @Id
    var id: ObjectId? = null,

    @Field(name = "first_name")
    var firstName: String,
    var surname: String,
    var patronymic: String? = null,
    var email: String,

    @Field(name = "phone_number")
    var phoneNumber: String? = null,
    var birthday: LocalDate? = null,
    var comment: String? = null
)
