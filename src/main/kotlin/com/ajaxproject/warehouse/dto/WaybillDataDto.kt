package com.ajaxproject.warehouse.dto

import com.ajaxproject.warehouse.entity.Customer
import com.ajaxproject.warehouse.entity.WaybillProduct
import java.time.LocalDate

data class WaybillDataDto(
    val id: Int,
    val customer: Customer,
    val date: LocalDate?,
    val productList: List<WaybillProduct>,
    val totalPrice: Double
)
