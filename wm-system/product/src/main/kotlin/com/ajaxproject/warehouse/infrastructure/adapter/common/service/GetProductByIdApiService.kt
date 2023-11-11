package com.ajaxproject.warehouse.infrastructure.adapter.common.service

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.mapper.mapToProto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class GetProductByIdApiService(
    private val productServiceInPort: ProductServiceInPort
) {
    fun getById(id: String): Mono<GetProductByIdResponse> =
        id.toMono()
            .flatMap { productServiceInPort.getById(id) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(product: Product): GetProductByIdResponse =
        GetProductByIdResponse.newBuilder().apply {
            successBuilder.product = product.mapToProto()
        }.build()

    private fun buildFailureResponse(exception: Throwable): GetProductByIdResponse =
        GetProductByIdResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
