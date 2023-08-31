package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.Waybill
import com.ajaxproject.warehouse.entity.WaybillProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WaybillProductRepository : JpaRepository<WaybillProduct, Int> {
    fun findByWaybill(waybill: Waybill): List<WaybillProduct>
}