package com.ajaxproject.warehouse.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CustomerUpdateDto(
    @field:NotNull(message = "id must be provided")
    @field:Min(1, message = "id must be valid")
    val id: Int?,
    @field:NotNull(message = "firstName must be provided")
    @field:NotEmpty(message = "firstName must be not empty")
    val firstName: String?,
    @field:NotNull(message = "surname must be provided")
    @field:NotEmpty(message = "surname must be not empty")
    val surname: String?,
    val patronymic: String?,
    @field:NotNull(message = "email must be provided")
    @field:NotEmpty(message = "email must be not empty")
    val email: String?,
    val phoneNumber: String?,
    val birthday: LocalDate?,
    val comment: String?
)
