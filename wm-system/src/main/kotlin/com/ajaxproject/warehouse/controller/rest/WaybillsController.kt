package com.ajaxproject.warehouse.controller.rest

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.dto.WaybillInfoUpdateDto
import com.ajaxproject.warehouse.service.WaybillService
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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/waybills")
class WaybillsController(
    val waybillService: WaybillService
) {

    @GetMapping  // TODO investigate about auto caching
    fun findAllWaybills(): Flux<WaybillDataLiteDto> = waybillService.findAllWaybills()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<WaybillDataDto> = waybillService.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybill(@RequestBody @Valid createDto: WaybillCreateDto): Mono<WaybillDataDto> =
        waybillService.createWaybill(createDto)

    @PutMapping("/{id}")
    fun updateWaybillInfo(
        @RequestBody @Valid infoUpdateDto: WaybillInfoUpdateDto,
        @PathVariable id: String
    ): Mono<WaybillDataDto> = waybillService.updateWaybillInfo(infoUpdateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybill(@PathVariable id: String): Mono<Unit> = waybillService.deleteById(id)
}
