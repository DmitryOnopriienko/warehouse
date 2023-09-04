package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.service.WaybillService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/waybills")
class WaybillsController(val waybillService: WaybillService) {

    @GetMapping
    fun findAll() = waybillService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int) = waybillService.findById(id)
}
