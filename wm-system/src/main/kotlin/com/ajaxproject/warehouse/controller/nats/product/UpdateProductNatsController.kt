package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.dto.mapToDto
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class UpdateProductNatsController(
    val productService: ProductService
) : NatsController<UpdateProductRequest, UpdateProductResponse> {

    override val subject: String = NatsSubject.Product.UPDATE

    override val parser: Parser<UpdateProductRequest> = UpdateProductRequest.parser()

    override fun handle(request: UpdateProductRequest): Mono<UpdateProductResponse> =
        Mono.fromCallable {
            require (request.hasId()) { "id must be provided" }
            request
        }
            .flatMap { productService.updateProduct(request.mapToDto(), request.id) }
            .map { buildSuccessResponse(it.mapToProto()) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    fun buildSuccessResponse(product: Product): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            successBuilder.product = product
        }.build()

    fun buildFailureResponse(exception: Throwable): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
