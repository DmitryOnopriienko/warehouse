package com.ajaxproject.warehouse.infrastructure.adapter.nats.controller

import com.ajaxproject.api.internal.warehousesvc.NatsSubject
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.UpdateProductApiAdapter
import com.ajaxproject.warehouse.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateProductNatsController(
    private val updateProductApiAdapter: UpdateProductApiAdapter
) : NatsController<UpdateProductRequest, UpdateProductResponse> {

    override val subject: String = NatsSubject.Product.UPDATE

    override val parser: Parser<UpdateProductRequest> = UpdateProductRequest.parser()

    override fun handle(request: UpdateProductRequest): Mono<UpdateProductResponse> =
        updateProductApiAdapter.updateProduct(request)
}
