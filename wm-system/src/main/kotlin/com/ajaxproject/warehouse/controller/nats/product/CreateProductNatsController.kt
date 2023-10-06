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

@Component
class CreateProductNatsController(
    val productService: ProductService
) : NatsController<CreateProductRequest, CreateProductResponse> {

    override val subject: String = CREATE

    override val parser: Parser<CreateProductRequest> = CreateProductRequest.parser()

    override fun handle(request: CreateProductRequest): CreateProductResponse = runCatching {
        buildSuccessResponse(
            productService
                .createProduct(request.mapToDto())  // TODO add validation
                .mapToProto()
        )
    }.getOrElse { exception ->
        buildFailureResponse(exception)
    }

    fun buildSuccessResponse(product: Product): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            successBuilder.setProduct(product)
        }.build()

    fun buildFailureResponse(exception: Throwable): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            failureBuilder.setMessage("Exception encountered: $exception")
        }.build()
}
