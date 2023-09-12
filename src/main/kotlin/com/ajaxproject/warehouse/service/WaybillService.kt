package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto

interface WaybillService {
    fun findAll(): List<WaybillDataLiteDto>

    fun findById(id: Int): WaybillDataDto

    fun createWaybill(createDto: WaybillCreateDto): WaybillDataDto

    fun deleteById(id: Int)
    fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: Int): WaybillDataLiteDto
}
