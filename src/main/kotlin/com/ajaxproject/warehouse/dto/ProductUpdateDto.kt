package com.ajaxproject.warehouse.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ProductUpdateDto(
    @field:NotNull(message = "id must be provided")
    @field:Min(1, message = "id must be valid")
    val id: Int?,
    @field:NotNull(message = "title must be provided")
    @field:NotEmpty(message = "title must be provided")
    val title: String?,
    @field:NotNull(message = "price must be provided")
    @field:DecimalMin(value = "0.01", message = "price must be more than 0.01")
    val price: Double?,
    @field:NotNull(message = "amount must be provided")
    val amount: Int?,
    val about: String?
)
