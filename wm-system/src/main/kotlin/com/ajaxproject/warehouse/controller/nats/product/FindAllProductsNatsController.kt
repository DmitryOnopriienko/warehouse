package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.warehouse.controller.nats.NatsController
import com.ajaxproject.warehouse.service.ProductService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class FindAllProductsNatsController(
    val productService: ProductService
) : NatsController<FindAllProductsRequest, FindAllProductsResponse> {

    override val subject: String = NatsSubject.Product.FIND_ALL

    override val parser: Parser<FindAllProductsRequest> = FindAllProductsRequest.parser()

    override fun handle(request: FindAllProductsRequest): Mono<FindAllProductsResponse> =
        request.toMono()
            .flatMap { productService.findAllProducts().collectList() }
            .map { products -> buildSuccessResponse(products.map { it.mapToProto() }) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    fun buildSuccessResponse(products: List<Product>): FindAllProductsResponse =
        FindAllProductsResponse.newBuilder().apply {
            successBuilder
                .productsBuilder
                .addAllProduct(products)
        }.build()

    fun buildFailureResponse(exception: Throwable): FindAllProductsResponse =
        FindAllProductsResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
