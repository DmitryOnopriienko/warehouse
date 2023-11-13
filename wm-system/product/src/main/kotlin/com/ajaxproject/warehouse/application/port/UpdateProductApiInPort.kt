package com.ajaxproject.warehouse.application.port

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import reactor.core.publisher.Mono

interface UpdateProductApiInPort {
    fun updateProduct(productRequest: UpdateProductRequest): Mono<UpdateProductResponse>
}
