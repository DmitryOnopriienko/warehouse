package com.ajaxproject.warehouse.dto

data class ProductDataDto(
    var id: Int?,
    var title: String,
    var price: Double,
    var amount: Int,
    var about: String?
)
