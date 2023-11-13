package com.ajaxproject.warehouse.infrastructure.adapter.common.service

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.warehouse.application.port.FindAllProductsApiInPort
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.mapper.mapToProto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class FindAllProductsApiService(
    private val productServiceInPort: ProductServiceInPort
) : FindAllProductsApiInPort {
    override fun findAll(): Mono<FindAllProductsResponse> =
        productServiceInPort.findAllProducts()
            .collectList()
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(products: List<Product>): FindAllProductsResponse =
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
