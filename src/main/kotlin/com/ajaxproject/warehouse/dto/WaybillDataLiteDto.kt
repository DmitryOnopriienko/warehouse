package com.ajaxproject.warehouse.dto

import com.ajaxproject.warehouse.entity.Customer
import java.time.LocalDate

data class WaybillDataLiteDto(
    val id: Int?,
    val customer: Customer,
    val date: LocalDate?,
    val totalPrice: Double
)
