package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import com.ajaxproject.warehouse.service.WaybillServiceMongo
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/waybills")
class WaybillsController(
    val waybillServiceMongo: WaybillServiceMongo
) {

    @GetMapping
    fun findAllWaybillsMongo(): List<WaybillDataLiteDto> = waybillServiceMongo.findAllWaybills()

    @GetMapping("/{id}")
    fun findByIdMongo(@PathVariable id: String): WaybillDataDto = waybillServiceMongo.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybillMongo(@RequestBody @Valid createDto: WaybillCreateDto): WaybillDataDto =
        waybillServiceMongo.createWaybill(createDto)

    @PutMapping("/{id}")
    fun updateWaybillInfoMongo(
        @RequestBody @Valid infoUpdateDto: WaybillInfoUpdateDto,
        @PathVariable id: String
    ): WaybillDataDto = waybillServiceMongo.updateWaybillInfo(infoUpdateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybillMongo(@PathVariable id: String): Unit  = waybillServiceMongo.deleteById(id)
}
