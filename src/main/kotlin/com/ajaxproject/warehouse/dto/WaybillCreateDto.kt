package com.ajaxproject.warehouse.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class WaybillCreateDto(
    @field:NotEmpty(message = "customerId must be provided")
    val customerId: String?,
    @field:NotNull(message = "date must be provided")
    val date: LocalDate?,
    @field:Valid
    val products: List<MongoWaybillProductCreateDto> = emptyList()
) {
    data class MongoWaybillProductCreateDto(
        @field:NotEmpty(message = "productId must be provided")
        val productId: String?,
        @field:NotNull(message = "amount must be provided")
        val amount: Int?
    )
}
