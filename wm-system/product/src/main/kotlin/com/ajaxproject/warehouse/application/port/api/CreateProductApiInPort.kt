package com.ajaxproject.warehouse.application.port.api

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import reactor.core.publisher.Mono

interface CreateProductApiInPort {
    fun createProduct(productRequest: CreateProductRequest): Mono<CreateProductResponse>
}
