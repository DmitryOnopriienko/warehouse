package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto

interface WaybillService {
    fun findAll(): List<WaybillDataLiteDto>

    fun findById(id: Int): WaybillDataDto
}
