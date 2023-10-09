package com.ajaxproject.warehouse.dto

import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product

data class ProductDataDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int,
    val about: String?
) {
    fun mapToProto(): Product = Product.newBuilder().also {
        it.id = id
        it.title = title
        it.price = price
        it.amount = amount
        it.about = about
    }.build()
}
