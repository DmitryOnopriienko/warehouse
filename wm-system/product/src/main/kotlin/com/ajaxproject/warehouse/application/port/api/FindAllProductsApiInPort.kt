package com.ajaxproject.warehouse.application.port.api

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import reactor.core.publisher.Mono

interface FindAllProductsApiInPort {
    fun findAll(): Mono<FindAllProductsResponse>
}
