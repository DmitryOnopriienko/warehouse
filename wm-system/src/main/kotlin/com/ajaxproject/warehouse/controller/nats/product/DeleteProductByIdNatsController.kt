package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.DELETE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class DeleteProductByIdNatsController(
    val productService: ProductService
) : NatsController<DeleteProductByIdRequest, DeleteProductByIdResponse> {

    override val subject: String = DELETE

    override val parser: Parser<DeleteProductByIdRequest> = DeleteProductByIdRequest.parser()

    override fun handle(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        runCatching {
            productService.deleteByIdR(request.id)
                .map { buildSuccessResponse() }
                .onErrorResume { buildFailureResponse(it).toMono() }
        }.getOrElse { exception ->
            buildFailureResponse(exception).toMono()
        }

    fun buildSuccessResponse(): DeleteProductByIdResponse =
        DeleteProductByIdResponse.newBuilder().apply {
            successBuilder.build()
        }.build()

    fun buildFailureResponse(exception: Throwable): DeleteProductByIdResponse =
        DeleteProductByIdResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
