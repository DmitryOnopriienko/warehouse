package com.ajaxproject.warehouse.service.grpc

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class FindAllProductsGrpcService(
    private val productService: ProductService
) {
    fun findAll(): Mono<FindAllProductsResponse> =
        productService.findAllProducts()
            .collectList()
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(products: List<ProductDataLiteDto>): FindAllProductsResponse =
        FindAllProductsResponse.newBuilder().apply {
            successBuilder.productsBuilder
                .addAllProduct(
                    products.map { product -> product.mapToProto() }
                )
        }.build()

    private fun buildFailureResponse(exception: Throwable): FindAllProductsResponse =
        FindAllProductsResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
