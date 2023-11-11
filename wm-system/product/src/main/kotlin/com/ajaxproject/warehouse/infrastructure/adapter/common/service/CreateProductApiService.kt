package com.ajaxproject.warehouse.infrastructure.adapter.common.service

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.mapper.mapToDomain
import com.ajaxproject.warehouse.infrastructure.mapper.mapToProto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class CreateProductApiService(
    private val productServiceInPort: ProductServiceInPort
) {
    fun createProduct(productRequest: CreateProductRequest): Mono<CreateProductResponse> =
        productRequest.toMono()
            .flatMap { productServiceInPort.createProduct(productRequest.mapToDomain()) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(product: Product): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            successBuilder.product = product.mapToProto()
        }.build()

    private fun buildFailureResponse(exception: Throwable): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
