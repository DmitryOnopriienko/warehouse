package com.ajaxproject.warehouse.dto

import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product

data class ProductDataLiteDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int
) {
    fun mapToProto(): Product = Product.newBuilder()
        .setId(id)
        .setTitle(title)
        .setPrice(price)
        .setAmount(amount)
        .build()
}
