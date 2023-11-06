package com.ajaxproject.warehouse.service.grpc

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.stereotype.Service
import reactor.kotlin.core.publisher.toMono

@Service
class DeleteProductGrpcService(
    private val productService: ProductService
) {
    fun deleteProduct(request: DeleteProductByIdRequest) =
        request.toMono()
            .flatMap { productService.deleteById(request.id) }
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
