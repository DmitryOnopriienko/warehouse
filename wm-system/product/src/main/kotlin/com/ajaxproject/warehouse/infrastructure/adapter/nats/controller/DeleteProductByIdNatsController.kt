package com.ajaxproject.warehouse.infrastructure.adapter.nats.controller

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.DELETE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.DeleteProductApiService
import com.ajaxproject.warehouse.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteProductByIdNatsController(
    private val deleteProductApiService: DeleteProductApiService
) : NatsController<DeleteProductByIdRequest, DeleteProductByIdResponse> {

    override val subject: String = DELETE

    override val parser: Parser<DeleteProductByIdRequest> = DeleteProductByIdRequest.parser()

    override fun handle(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        deleteProductApiService.deleteProduct(request)
}
