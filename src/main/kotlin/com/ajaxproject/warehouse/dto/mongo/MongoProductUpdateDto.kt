package com.ajaxproject.warehouse.dto.mongo

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class MongoProductUpdateDto(
    @field:NotNull(message = "id must be provided")
    val id: String?,
    @field:NotEmpty(message = "title must be provided")
    val title: String?,
    @field:NotNull(message = "price must be provided")
    @field:DecimalMin(value = "0.01", message = "price must be more than 0.01")
    val price: Double?,
    @field:NotNull(message = "amount must be provided")
    val amount: Int?,
    val about: String?
)
