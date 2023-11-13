package com.ajaxproject.warehouse.application.port

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import reactor.core.publisher.Mono

interface GetProductByIdApiInPort {
    fun getById(id: String): Mono<GetProductByIdResponse>
}
