package com.ajaxproject.warehouse.dto.mongo

import com.ajaxproject.warehouse.entity.MongoWaybill
import java.time.LocalDate

data class MongoCustomerDataDto(
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
