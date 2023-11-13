package com.ajaxproject.warehouse.infrastructure.adapter.common.service

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class DeleteProductApiAdapter(
    private val productServiceInPort: ProductServiceInPort
) {
    fun deleteProduct(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        request.toMono()
            .flatMap { productServiceInPort.deleteById(request.id) }
            .map { buildSuccessResponse() }
            .onErrorResume { buildFailureResponse(it).toMono() }

    private fun buildSuccessResponse(): DeleteProductByIdResponse =
        DeleteProductByIdResponse.newBuilder().apply {
            successBuilder.build()
        }.build()

    private fun buildFailureResponse(exception: Throwable): DeleteProductByIdResponse =
        DeleteProductByIdResponse.newBuilder().apply {
            failureBuilder.message = "Exception encountered: $exception"
        }.build()
}
