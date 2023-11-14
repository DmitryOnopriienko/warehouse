package com.ajaxproject.warehouse.infrastructure.adapter.common.service

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.mapper.mapToDomain
import com.ajaxproject.warehouse.infrastructure.mapper.mapToProto
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class UpdateProductApiAdapter(
    private val productServiceInPort: ProductServiceInPort
) {
    fun updateProduct(productRequest: UpdateProductRequest): Mono<UpdateProductResponse> =
        productRequest.toMono()
            .flatMap {
                require(productRequest.hasId()) { "Product ID cannot be empty" }
                productServiceInPort.updateProduct(
                    productToUpdate = productRequest.mapToDomain(),
                    id = productRequest.id
                )
            }
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(product: Product): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            successBuilder.product = product.mapToProto()
        }.build()

    private fun buildFailureResponse(exception: Throwable): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
