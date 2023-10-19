package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class GetProductByIdNatsController(
    val productService: ProductService
) : NatsController<GetProductByIdRequest, GetProductByIdResponse> {

    override val subject: String = NatsSubject.Product.GET_BY_ID

    override val parser: Parser<GetProductByIdRequest> = GetProductByIdRequest.parser()

    override fun handle(request: GetProductByIdRequest): Mono<GetProductByIdResponse> =
        runCatching {
            productService.getById(request.id)
                .map { buildSuccessResponse(it.mapToProto()) }
                .onErrorResume { buildFailureResponse(it).toMono() }
        }.getOrElse { exception ->
            buildFailureResponse(exception).toMono()
        }

    fun buildSuccessResponse(product: Product): GetProductByIdResponse =
        GetProductByIdResponse.newBuilder().apply {
            successBuilder.product = product
        }.build()

    fun buildFailureResponse(exception: Throwable): GetProductByIdResponse =
        GetProductByIdResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
