package com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.mapper

import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto.ProductDataDto
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto.ProductSaveDto

fun ProductSaveDto.mapToDomain() = Product(
    title = title as String,
    price = price as Double,
    amount = amount as Int,
    about = about
)

fun Product.mapToDataDto() = ProductDataDto(
    id = id as String,
    title = title,
    price = price,
    amount = amount,
    about = about
)

fun Product.mapToLiteDto() = ProductDataLiteDto(
    id = id as String,
    title = title,
    price = price,
    amount = amount
)
