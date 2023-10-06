package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.FIND_ALL
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component

@Component
class FindAllProductsNatsController(
    val productService: ProductService
) : NatsController<FindAllProductsRequest, FindAllProductsResponse> {

    override val subject: String = FIND_ALL

    override val parser: Parser<FindAllProductsRequest> = FindAllProductsRequest.parser()

    override fun handle(request: FindAllProductsRequest): FindAllProductsResponse {
        return runCatching {
            buildSuccessResponse(
                productService
                    .findAllProducts()
                    .map { it.mapToProto() })
        }.getOrElse { exception ->
            buildFailureResponse(exception)
        }
    }

    fun buildSuccessResponse(products: List<Product>): FindAllProductsResponse =
        FindAllProductsResponse.newBuilder().apply {
            successBuilder
                .productsBuilder
                .addAllProduct(products)
        }.build()

    fun buildFailureResponse(exception: Throwable): FindAllProductsResponse =
        FindAllProductsResponse.newBuilder().apply {
            failureBuilder.setMessage("Exception encountered: $exception")
        }.build()
}
