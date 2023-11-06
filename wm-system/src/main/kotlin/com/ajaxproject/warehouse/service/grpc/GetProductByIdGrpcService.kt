package com.ajaxproject.warehouse.service.grpc

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class GetProductByIdGrpcService(
    private val productService: ProductService
) {
    fun getById(id: String): Mono<GetProductByIdResponse> =
        id.toMono()
            .flatMap { productService.getById(id) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(product: ProductDataDto): GetProductByIdResponse =
        GetProductByIdResponse.newBuilder().apply {
            successBuilder.product = product.mapToProto()
        }.build()

    private fun buildFailureResponse(exception: Throwable): GetProductByIdResponse =
        GetProductByIdResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
