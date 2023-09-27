package com.ajaxproject.warehouse.dto

import java.time.LocalDate

data class WaybillDataDto(
    val id: String,
    val customer: CustomerDataLiteDto,
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
