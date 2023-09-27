package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.dto.mongo.MongoWaybillCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillInfoUpdateDto
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

    @GetMapping("/mongo")
    fun findAllWaybillsMongo(): List<MongoWaybillDataLiteDto> = waybillServiceMongo.findAllWaybills()

    @GetMapping("/mongo/{id}")
    fun findByIdMongo(@PathVariable id: String): MongoWaybillDataDto = waybillServiceMongo.getById(id)

    @PostMapping("/mongo/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybillMongo(@RequestBody @Valid createDto: MongoWaybillCreateDto): MongoWaybillDataDto =
        waybillServiceMongo.createWaybill(createDto)

    @PutMapping("/mongo/{id}")
    fun updateWaybillInfoMongo(
        @RequestBody @Valid infoUpdateDto: MongoWaybillInfoUpdateDto,
        @PathVariable id: String
    ): MongoWaybillDataDto = waybillServiceMongo.updateWaybillInfo(infoUpdateDto, id)

    @DeleteMapping("/mongo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybillMongo(@PathVariable id: String): Unit  = waybillServiceMongo.deleteById(id)
}
