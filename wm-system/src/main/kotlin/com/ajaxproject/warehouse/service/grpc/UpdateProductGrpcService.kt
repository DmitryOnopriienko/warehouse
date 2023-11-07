package com.ajaxproject.warehouse.service.grpc

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.mapToDto
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UpdateProductGrpcService(
    private val productService: ProductService
) {
    fun updateProduct(productRequest: UpdateProductRequest): Mono<UpdateProductResponse> =
        productRequest.toMono()
            .flatMap {
                productService.updateProduct(
                    updateDto = productRequest.mapToDto(),
                    id = productRequest.id
                )
            }
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(product: ProductDataDto): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            successBuilder.product = product.mapToProto()
        }.build()

    private fun buildFailureResponse(exception: Throwable): UpdateProductResponse =
        UpdateProductResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
