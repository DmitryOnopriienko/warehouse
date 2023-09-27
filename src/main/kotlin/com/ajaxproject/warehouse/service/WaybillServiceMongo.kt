package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.mongo.MongoWaybillCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoWaybillDataLiteDto

interface WaybillServiceMongo {
    fun findAllWaybills(): List<MongoWaybillDataLiteDto>
    fun getById(id: String): MongoWaybillDataDto
    fun createWaybill(createDto: MongoWaybillCreateDto): MongoWaybillDataDto
}
