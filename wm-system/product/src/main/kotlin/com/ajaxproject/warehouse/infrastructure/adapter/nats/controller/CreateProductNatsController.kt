package com.ajaxproject.warehouse.infrastructure.adapter.nats.controller

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.CREATE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.CreateProductApiService
import com.ajaxproject.warehouse.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateProductNatsController(
    private val createProductApiService: CreateProductApiService
) : NatsController<CreateProductRequest, CreateProductResponse> {

    override val subject: String = CREATE

    override val parser: Parser<CreateProductRequest> = CreateProductRequest.parser()

    override fun handle(request: CreateProductRequest): Mono<CreateProductResponse> =
        createProductApiService.createProduct(request)
}
