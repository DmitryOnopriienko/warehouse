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

    @GetMapping
    fun findAllWaybills(): List<WaybillDataLiteDto> = waybillService.findAllWaybills()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): WaybillDataDto = waybillService.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybill(@RequestBody @Valid createDto: WaybillCreateDto): WaybillDataDto =
        waybillService.createWaybill(createDto)

    @PutMapping("/{id}")
    fun updateWaybillInfo(
        @RequestBody @Valid infoUpdateDto: WaybillInfoUpdateDto,
        @PathVariable id: String
    ): WaybillDataDto = waybillService.updateWaybillInfo(infoUpdateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybill(@PathVariable id: String): Unit  = waybillService.deleteById(id)

    @GetMapping("/r/")  // TODO investigate about auto caching
    fun findAllWaybillsR(): Flux<WaybillDataLiteDto> = waybillService.findAllWaybillsR()

    @GetMapping("/r/{id}")
    fun findByIdR(@PathVariable id: String): Mono<WaybillDataDto> = waybillService.getByIdR(id)

    @PostMapping("/r/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWaybillR(@RequestBody @Valid createDto: WaybillCreateDto): Mono<WaybillDataDto> =
        waybillService.createWaybillR(createDto)

    @PutMapping("/r/{id}")
    fun updateWaybillInfoR(
        @RequestBody @Valid infoUpdateDto: WaybillInfoUpdateDto,
        @PathVariable id: String
    ): Mono<WaybillDataDto> = waybillService.updateWaybillInfoR(infoUpdateDto, id)

    @DeleteMapping("/r/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWaybillR(@PathVariable id: String): Unit  = waybillService.deleteByIdR(id)
}
