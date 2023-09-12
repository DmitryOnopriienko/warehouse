package com.ajaxproject.warehouse.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class WaybillInfoUpdateDto(
    @field:NotNull(message = "id must be provided")
    @field:Min(1, message = "id must be valid")
    val id: Int?,
    @field:NotNull(message = "customerId must be provided")
    @field:Min(1, message = "customerId must be valid")
    val customerId: Int?,
    @field:NotNull(message = "date must be provided")
    val date: LocalDate?
)
