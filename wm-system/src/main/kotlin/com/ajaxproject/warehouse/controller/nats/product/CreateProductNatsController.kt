package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.CREATE
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.dto.mapToDto
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class CreateProductNatsController(
    val productService: ProductService
) : NatsController<CreateProductRequest, CreateProductResponse> {

    override val subject: String = CREATE

    override val parser: Parser<CreateProductRequest> = CreateProductRequest.parser()

    override fun handle(request: CreateProductRequest): Mono<CreateProductResponse> =
        request.toMono()    // TODO ask if wrapping in Mono is necessary
            .flatMap { productService.createProduct(request.mapToDto()) }
            .map { buildSuccessResponse(it.mapToProto()) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    fun buildSuccessResponse(product: Product): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            successBuilder.product = product
        }.build()

    fun buildFailureResponse(exception: Throwable): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
