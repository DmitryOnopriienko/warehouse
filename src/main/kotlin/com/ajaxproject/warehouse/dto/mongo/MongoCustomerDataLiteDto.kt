package com.ajaxproject.warehouse.dto.mongo

data class MongoCustomerDataLiteDto(
    val id: String,
    val firstName: String,
    val surname: String,
    val patronymic: String? = null,
    val email: String,
    val phoneNumber: String? = null
)
