package com.ajaxproject.warehouse.dto

import com.ajaxproject.warehouse.entity.MongoWaybill
import java.time.LocalDate

data class CustomerDataDto(
    val id: String,
    val firstName: String,
    val surname: String,
    val patronymic: String? = null,
    val email: String,
    val phoneNumber: String? = null,
    val birthday: LocalDate? = null,
    val comment: String? = null,
    val waybills: List<MongoWaybill> = listOf()
)
