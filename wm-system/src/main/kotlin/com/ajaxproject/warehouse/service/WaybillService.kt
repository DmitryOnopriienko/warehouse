package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface WaybillService {

    fun findAllWaybills(): List<WaybillDataLiteDto>

    fun getById(id: String): WaybillDataDto

    fun createWaybill(createDto: WaybillCreateDto): WaybillDataDto

    fun deleteById(id: String)

    fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: String): WaybillDataDto

    fun findAllWaybillsR(): Flux<WaybillDataLiteDto>

    fun getByIdR(id: String): Mono<WaybillDataDto>

    fun createWaybillR(createDto: WaybillCreateDto): Mono<WaybillDataDto>

    fun deleteByIdR(id: String): Mono<Unit>

    fun updateWaybillInfoR(infoUpdateDto: WaybillInfoUpdateDto, id: String): Mono<WaybillDataDto>
}
