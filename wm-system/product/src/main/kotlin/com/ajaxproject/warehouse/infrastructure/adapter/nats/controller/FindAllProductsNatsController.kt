package com.ajaxproject.warehouse.infrastructure.adapter.nats.controller

import com.ajaxproject.api.internal.warehousesvc.NatsSubject
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.warehouse.application.port.FindAllProductsApiInPort
import com.ajaxproject.warehouse.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllProductsNatsController(
    private val findAllProductsApiInPort: FindAllProductsApiInPort
) : NatsController<FindAllProductsRequest, FindAllProductsResponse> {

    override val subject: String = NatsSubject.Product.FIND_ALL

    override val parser: Parser<FindAllProductsRequest> = FindAllProductsRequest.parser()

    override fun handle(request: FindAllProductsRequest): Mono<FindAllProductsResponse> =
        findAllProductsApiInPort.findAll()
}
