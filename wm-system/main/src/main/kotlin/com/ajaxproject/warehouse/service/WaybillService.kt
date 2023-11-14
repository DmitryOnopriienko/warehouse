package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface WaybillService {

    fun findAllWaybills(): Flux<WaybillDataLiteDto>

    fun getById(id: String): Mono<WaybillDataDto>

    fun createWaybill(createDto: WaybillCreateDto): Mono<WaybillDataDto>

    fun deleteById(id: String): Mono<Unit>

    fun updateWaybillInfo(infoUpdateDto: WaybillInfoUpdateDto, id: String): Mono<WaybillDataDto>
}
