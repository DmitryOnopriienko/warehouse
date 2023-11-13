package com.ajaxproject.warehouse.infrastructure.adapter.nats.controller

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.DELETE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.application.port.api.DeleteProductApiInPort
import com.ajaxproject.warehouse.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteProductByIdNatsController(
    private val deleteProductApiInPort: DeleteProductApiInPort
) : NatsController<DeleteProductByIdRequest, DeleteProductByIdResponse> {

    override val subject: String = DELETE

    override val parser: Parser<DeleteProductByIdRequest> = DeleteProductByIdRequest.parser()

    override fun handle(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        deleteProductApiInPort.deleteProduct(request)
}
