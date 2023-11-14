package com.ajaxproject.warehouse.dto

import com.ajaxproject.api.internal.warehousesvc.commonmodels.waybill.Waybill
import java.time.LocalDate
import java.time.ZoneOffset

data class WaybillDataDto(
    val id: String,
    val customer: CustomerDataLiteDto,
    val date: LocalDate,
    val totalPrice: Double,
    val products: List<WaybillProductDataDto>
) {
    data class WaybillProductDataDto(
        val id: String,
        val title: String,
        val price: Double,
        val orderedAmount: Int
    )

    fun mapToProto(): Waybill = Waybill.newBuilder().also {
        it.id = id
        it.customerId = customer.id
        it.dateBuilder.seconds = date.atStartOfDay().atOffset(ZoneOffset.UTC).toEpochSecond()
    }.build()
}
