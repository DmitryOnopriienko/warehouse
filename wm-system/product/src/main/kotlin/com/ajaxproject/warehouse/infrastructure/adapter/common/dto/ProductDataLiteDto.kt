package com.ajaxproject.warehouse.infrastructure.adapter.common.dto

import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product

data class ProductDataLiteDto(
    val id: String,
    val title: String,
    val price: Double,
    val amount: Int
) {
    fun mapToProto(): Product = Product.newBuilder().also {
        it.id = id
        it.title = title
        it.price = price
        it.amount = amount
    }.build()
}
