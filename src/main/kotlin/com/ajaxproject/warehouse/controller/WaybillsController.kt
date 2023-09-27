package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillInfoUpdateDto
import com.ajaxproject.warehouse.service.WaybillService
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
    val waybillService: WaybillService,
    val waybillServiceMongo: WaybillServiceMongo
) {

    @GetMapping
    fun findAll(): List<WaybillDataLiteDto> = waybillService.findAll()

    @GetMapping("/mongo")
    fun findAllWaybillsMongo(): List<MongoWaybillDataLiteDto> = waybillServiceMongo.findAllWaybills()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): WaybillDataDto = waybillService.findById(id)

    @GetMapping("/mongo/{id}")
    fun findByIdMongo(@PathVariable id: String): MongoWaybillDataDto = waybillServiceMongo.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybill(@RequestBody @Valid createDto: WaybillCreateDto): WaybillDataDto =
        waybillService.createWaybill(createDto)

    @PostMapping("/mongo/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybillMongo(@RequestBody @Valid createDto: MongoWaybillCreateDto): MongoWaybillDataDto =
        waybillServiceMongo.createWaybill(createDto)

    @PutMapping("/{id}")
    fun updateWaybillData(
        @RequestBody @Valid infoUpdateDto: WaybillInfoUpdateDto,
        @PathVariable id: Int
    ): WaybillDataLiteDto = waybillService.updateWaybillInfo(infoUpdateDto, id)

    @PutMapping("/mongo/{id}")
    fun updateWaybillInfoMongo(
        @RequestBody @Valid infoUpdateDto: MongoWaybillInfoUpdateDto,
        @PathVariable id: String
    ): MongoWaybillDataDto = waybillServiceMongo.updateWaybillInfo(infoUpdateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybill(@PathVariable id: Int): Unit = waybillService.deleteById(id)

    @DeleteMapping("/mongo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybillMongo(@PathVariable id: String): Unit  = waybillServiceMongo.deleteById(id)
}
