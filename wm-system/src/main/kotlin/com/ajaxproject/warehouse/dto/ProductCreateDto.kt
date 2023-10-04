package com.ajaxproject.warehouse.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ProductCreateDto(
    @field:NotEmpty(message = "title must be provided")
    val title: String?,
    @field:NotNull(message = "price must be provided")
    @field:DecimalMin(value = "0.01", message = "price must be more than 0.01")
    val price: Double?,
    @field:Min(0, message = "amount must be 0 or more")
    val amount: Int = 0,
    val about: String?
)
