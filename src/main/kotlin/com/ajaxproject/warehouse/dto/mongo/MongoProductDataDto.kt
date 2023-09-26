package com.ajaxproject.warehouse.dto.mongo

data class MongoProductDataDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String?
)
