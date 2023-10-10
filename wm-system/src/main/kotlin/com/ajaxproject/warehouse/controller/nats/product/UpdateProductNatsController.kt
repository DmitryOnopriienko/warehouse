package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.UPDATE
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.dto.mapToDto
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component

@Component
class UpdateProductNatsController(
    val productService: ProductService
) : NatsController<UpdateProductRequest, UpdateProductResponse> {

    override val subject: String = UPDATE

    override val parser: Parser<UpdateProductRequest> = UpdateProductRequest.parser()

    override fun handle(request: UpdateProductRequest): UpdateProductResponse {
        if (!request.hasId()) return buildFailureResponse(IllegalArgumentException("id must be provided"))
        return runCatching {
            buildSuccessResponse(
                productService.updateProduct(
                    request.mapToDto(),
                    request.id
                ).mapToProto()
            )
        }.getOrElse { exception ->
            buildFailureResponse(exception)
        }
    }


    fun buildSuccessResponse(product: Product): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            successBuilder.product = product
        }.build()

    fun buildFailureResponse(exception: Throwable): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
