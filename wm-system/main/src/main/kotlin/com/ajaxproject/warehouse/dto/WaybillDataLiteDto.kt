package com.ajaxproject.warehouse.dto

import java.time.LocalDate

data class WaybillDataLiteDto(
    val id: String,
    val customerId: String,
    val date: LocalDate,
    val totalPrice: Double
)
