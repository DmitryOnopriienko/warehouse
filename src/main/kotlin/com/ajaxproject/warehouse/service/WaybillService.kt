package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto

interface WaybillService {

    fun findAllWaybills(): List<WaybillDataLiteDto>

    fun getById(id: String): WaybillDataDto

    fun createWaybill(createDto: WaybillCreateDto): WaybillDataDto

    fun deleteById(id: String)

    fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: String): WaybillDataDto
}
