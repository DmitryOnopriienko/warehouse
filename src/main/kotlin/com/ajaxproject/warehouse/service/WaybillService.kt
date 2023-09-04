package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.entity.Waybill

interface WaybillService {
    fun findAll(): List<Waybill>

    fun findById(id: Int): Waybill
}
