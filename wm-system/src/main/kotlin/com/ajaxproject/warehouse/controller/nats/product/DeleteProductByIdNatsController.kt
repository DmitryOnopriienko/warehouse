package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.DELETE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component

@Component
class DeleteProductByIdNatsController(
    val productService: ProductService
) : NatsController<DeleteProductByIdRequest, DeleteProductByIdResponse> {

    override val subject: String = DELETE

    override val parser: Parser<DeleteProductByIdRequest> = DeleteProductByIdRequest.parser()

    override fun handle(request: DeleteProductByIdRequest): DeleteProductByIdResponse =
        runCatching {
            productService.deleteById(request.id)
            buildSuccessResponse()
        }.getOrElse { exception ->
            buildFailureResponse(exception)
        }

    fun buildSuccessResponse(): DeleteProductByIdResponse =
        DeleteProductByIdResponse.newBuilder().apply {
            successBuilder.build()
        }.build()

    fun buildFailureResponse(exception: Throwable): DeleteProductByIdResponse =
        DeleteProductByIdResponse.newBuilder().apply {
            failureBuilder.setMessage("Exception encountered: $exception")
        }.build()
}
