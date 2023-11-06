package com.ajaxproject.warehouse.service.grpc

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.mapToDto
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class CreateProductGrpcService(
    private val productService: ProductService
) {
    fun createProduct(productRequest: CreateProductRequest): Mono<CreateProductResponse> =
        productRequest.toMono()
            .flatMap { productService.createProduct(productRequest.mapToDto()) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(product: ProductDataDto): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            successBuilder.product = product.mapToProto()
        }.build()

    private fun buildFailureResponse(exception: Throwable): CreateProductResponse =
        CreateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
