package com.ajaxproject.warehouse.infrastructure.adapter.nats.controller

import com.ajaxproject.api.internal.warehousesvc.NatsSubject
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.GetProductByIdApiAdapter
import com.ajaxproject.warehouse.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetProductByIdNatsController(
    private val getProductByIdApiAdapter: GetProductByIdApiAdapter
) : NatsController<GetProductByIdRequest, GetProductByIdResponse> {

    override val subject: String = NatsSubject.Product.GET_BY_ID

    override val parser: Parser<GetProductByIdRequest> = GetProductByIdRequest.parser()

    override fun handle(request: GetProductByIdRequest): Mono<GetProductByIdResponse> =
        getProductByIdApiAdapter.getById(request.id)
}
