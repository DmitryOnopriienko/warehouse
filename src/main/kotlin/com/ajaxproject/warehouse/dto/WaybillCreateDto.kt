package com.ajaxproject.warehouse.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class WaybillCreateDto(
    @field:NotNull(message = "customer id must be provided")
    @field:Min(1, message = "id must be valid")
    val customerId: Int?,
    @field:NotNull(message = "date must be provided")
    val date: LocalDate?,
    @field:Valid
    val products: List<WaybillProductCreateDto>? = null
) {
    data class WaybillProductCreateDto(
        @field:NotNull(message = "product id must be provided")
        @field:Min(1, message = "id must be valid")
        val id: Int?,
        @field:NotNull(message = "amount must be provided")
        val amount: Int?
    )
}
