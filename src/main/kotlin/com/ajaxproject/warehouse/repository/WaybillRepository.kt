package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.Customer
import com.ajaxproject.warehouse.entity.Waybill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WaybillRepository : JpaRepository<Waybill, Int> {
    fun findByCustomer(customer: Customer): List<Waybill>
}