package com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller

import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto.ProductDataDto
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.dto.ProductSaveDto
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.mapper.mapToDataDto
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.mapper.mapToDomain
import com.ajaxproject.warehouse.infrastructure.adapter.webflux.controller.mapper.mapToLiteDto
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
@RequestMapping("/products")
class ProductRestController(
    val productServiceInPort: ProductServiceInPort
) {
    @GetMapping
    fun findAllProducts(): Flux<ProductDataLiteDto> =
        productServiceInPort.findAllProducts()
            .map { it.mapToLiteDto() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ProductDataDto> =
        productServiceInPort.getById(id)
            .map { it.mapToDataDto() }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody @Valid createDto: ProductSaveDto): Mono<ProductDataDto> =
        productServiceInPort.createProduct(createDto.mapToDomain())
            .map { it.mapToDataDto() }

    @PutMapping("/{id}")
    fun updateProduct(
        @RequestBody @Valid updateDto: ProductSaveDto,
        @PathVariable id: String
    ): Mono<ProductDataDto> =
        productServiceInPort.updateProduct(updateDto.mapToDomain(), id)
            .map { it.mapToDataDto() }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: String): Mono<Unit> = productServiceInPort.deleteById(id)
}
