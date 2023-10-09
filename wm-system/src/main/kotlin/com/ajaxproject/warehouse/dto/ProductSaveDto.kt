package com.ajaxproject.warehouse.dto

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ProductSaveDto(
    @field:NotEmpty(message = "title must be provided")
    val title: String?,
    @field:NotNull(message = "price must be provided")
    @field:DecimalMin(value = "0.01", message = "price must be more than 0.01")
    val price: Double?,
    @field:NotNull(message = "amount must be provided")
    val amount: Int?,
    val about: String?
)

fun UpdateProductRequest.mapToDto(): ProductSaveDto = ProductSaveDto(
    title = title,
    price = price,
    amount = amount,
    about = about
)

fun CreateProductRequest.mapToDto(): ProductSaveDto = ProductSaveDto(
    title = title,
    price = price,
    amount = amount,
    about = about
)
