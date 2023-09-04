package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.entity.Waybill
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.WaybillRepository
import org.springframework.stereotype.Service

@Service
class WaybillServiceImpl(val waybillRepository: WaybillRepository) : WaybillService {
    override fun findAll(): List<Waybill> {
        return waybillRepository.findAll()
    }

    override fun findById(id: Int): Waybill {
        return waybillRepository.findById(id).orElseThrow { NotFoundException("Waybill with id $id not found") }
    }
}
