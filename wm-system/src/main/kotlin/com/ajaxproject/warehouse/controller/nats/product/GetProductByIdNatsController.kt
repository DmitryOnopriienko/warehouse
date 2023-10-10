package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.GET_BY_ID
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component

@Component
class GetProductByIdNatsController(
    val productService: ProductService
) : NatsController<GetProductByIdRequest, GetProductByIdResponse> {

    override val subject: String = GET_BY_ID

    override val parser: Parser<GetProductByIdRequest> = GetProductByIdRequest.parser()

    override fun handle(request: GetProductByIdRequest): GetProductByIdResponse {
        return runCatching {
            buildSuccessResponse(
                productService
                    .getById(request.id)
                    .mapToProto()
            )
        }.getOrElse { exception ->
            buildFailureResponse(exception)
        }
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
