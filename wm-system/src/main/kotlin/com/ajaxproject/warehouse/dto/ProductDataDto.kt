package com.ajaxproject.warehouse.dto

data class ProductDataDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String?
)
