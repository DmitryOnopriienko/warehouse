package com.ajaxproject.warehouse.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class WaybillInfoUpdateDto(
    @field:NotNull(message = "date must be provided")
    val date: LocalDate?,
    @field:NotEmpty(message = "customerId must be provided")
    val customerId: String?
)
