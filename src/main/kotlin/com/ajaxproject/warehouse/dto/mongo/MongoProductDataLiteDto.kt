package com.ajaxproject.warehouse.dto.mongo

data class MongoProductDataLiteDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int
)
