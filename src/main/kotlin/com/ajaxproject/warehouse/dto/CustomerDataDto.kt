package com.ajaxproject.warehouse.dto

import com.ajaxproject.warehouse.entity.Waybill
import java.time.LocalDate

data class CustomerDataDto(
    val id: Int?,
    val firstName: String,
    val surname: String,
    val patronymic: String?,
    val email: String,
    val phoneNumber: String?,
    val birthday: LocalDate?,
    val comment: String?,
    val waybills: List<Waybill>
)
