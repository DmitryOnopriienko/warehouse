package com.ajaxproject.warehouse.infrastructure.mapper

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product as ProtoProduct

fun CreateProductRequest.mapToDomain(): Product = Product(
    title = title,
    price = price,
    amount = amount,
    about = about
)

fun UpdateProductRequest.mapToDomain(): Product = Product(
    title = title,
    price = price,
    amount = amount,
    about = about
)

fun Product.mapToProto(): ProtoProduct = ProtoProduct.newBuilder().also {
    it.id = id
    it.title = title
    it.price = price
    it.amount = amount
    if (about != null) it.about = about
}.build()
