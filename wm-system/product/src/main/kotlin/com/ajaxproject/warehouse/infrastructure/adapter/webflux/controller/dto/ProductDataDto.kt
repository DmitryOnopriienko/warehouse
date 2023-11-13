package com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto

data class ProductDataDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String?
)
