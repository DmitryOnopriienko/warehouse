package com.ajaxproject.warehouse.dto

import jakarta.validation.constraints.NotEmpty
import java.time.LocalDate

data class CustomerCreateDto(
    @field:NotEmpty(message = "first name must be not empty")
    val firstName: String?,
    @field:NotEmpty(message = "surname must be provided")
    val surname: String?,
    val patronymic: String?,
    @field:NotEmpty(message = "email must be provided")
    val email: String?,
    val phoneNumber: String?,
    val birthday: LocalDate?,
    val comment: String?
)
