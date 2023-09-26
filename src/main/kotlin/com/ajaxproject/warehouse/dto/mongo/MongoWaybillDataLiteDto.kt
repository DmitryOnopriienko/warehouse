package com.ajaxproject.warehouse.dto.mongo

import java.time.LocalDate

data class MongoWaybillDataLiteDto(
    val id: String,
    val customerId: String,
    val date: LocalDate,
    val totalPrice: Double
)
