package com.ajaxproject.warehouse.application.port.api

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import reactor.core.publisher.Mono

interface DeleteProductApiInPort {
    fun deleteProduct(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse>
}
