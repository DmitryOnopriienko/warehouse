package com.ajaxproject.warehouse.dto

import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product

data class ProductDataDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String?
) {
    fun mapToProto(): Product = Product.newBuilder()
        .setId(id)
        .setTitle(title)
        .setPrice(price)
        .setAmount(amount)
        .setAbout(about)
        .build()
}
