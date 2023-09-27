package com.ajaxproject.warehouse.dto.mongo

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class MongoWaybillInfoUpdateDto(
    @field:NotEmpty(message = "id must be provided")
    val id: String?,
    @field:NotNull(message = "date must be provided")
    val date: LocalDate?,
    @field:NotEmpty(message = "customerId must be provided")
    val customerId: String?
)
