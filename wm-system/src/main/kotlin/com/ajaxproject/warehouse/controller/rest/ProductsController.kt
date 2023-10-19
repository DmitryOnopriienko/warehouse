package com.ajaxproject.warehouse.controller.rest

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductSaveDto
import com.ajaxproject.warehouse.service.ProductService
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
class ProductsController(
    val productService: ProductService
) {

    @GetMapping
    fun findAllProducts(): List<ProductDataLiteDto> = productService.findAllProducts()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ProductDataDto =
        productService.getById(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody @Valid createDto: ProductSaveDto): ProductDataDto =
        productService.createProduct(createDto)

    @PutMapping("/{id}")
    fun updateProduct(
        @RequestBody @Valid updateDto: ProductSaveDto,
        @PathVariable id: String
    ): ProductDataDto = productService.updateProduct(updateDto, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: String): Unit = productService.deleteById(id)

    @GetMapping("/r/")
    fun findAllProductsR(): Flux<ProductDataLiteDto> = productService.findAllProductsR()

    @GetMapping("/r/{id}")
    fun findByIdR(@PathVariable id: String): Mono<ProductDataDto> =
        productService.getByIdR(id)

    @PostMapping("/r/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProductR(@RequestBody @Valid createDto: ProductSaveDto): Mono<ProductDataDto> =
        productService.createProductR(createDto)

    @PutMapping("/r/{id}")
    fun updateProductR(
        @RequestBody @Valid updateDto: ProductSaveDto,
        @PathVariable id: String
    ): Mono<ProductDataDto> = productService.updateProductR(updateDto, id)

    @DeleteMapping("/r/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProductR(@PathVariable id: String): Mono<Unit> = productService.deleteByIdR(id)
}
