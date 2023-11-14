package com.ajaxproject.warehouse.domain

data class Product(
    val id: String? = null,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String? = null
) {

    init {
        require(title.isNotBlank()) { "title must be provided" }
        require(price >= MIN_PRICE) { "price must be provided" }
    }

    companion object {
        const val MIN_PRICE = 0.01
    }
}
