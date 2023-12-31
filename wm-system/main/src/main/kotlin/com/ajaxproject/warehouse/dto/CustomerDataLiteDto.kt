package com.ajaxproject.warehouse.dto

data class CustomerDataLiteDto(
    val id: String,
    val firstName: String,
    val surname: String,
    val patronymic: String?,
    val email: String,
    val phoneNumber: String?
)
