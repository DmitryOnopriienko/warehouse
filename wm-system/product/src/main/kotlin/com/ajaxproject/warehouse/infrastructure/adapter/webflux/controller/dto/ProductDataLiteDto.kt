package com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto

data class ProductDataLiteDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int
)
