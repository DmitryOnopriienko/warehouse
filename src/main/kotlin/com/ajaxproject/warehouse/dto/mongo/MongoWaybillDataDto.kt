package com.ajaxproject.warehouse.dto.mongo

import java.time.LocalDate

data class MongoWaybillDataDto(
    val id: String,
    val customer: MongoCustomerDataLiteDto,
    val date: LocalDate,
    val totalPrice: Double,
    val products: List<MongoWaybillProductDataDto>
) {
    data class MongoWaybillProductDataDto(
        val id: String,
        val title: String,
        val price: Double,
        val orderedAmount: Int
    )
}
