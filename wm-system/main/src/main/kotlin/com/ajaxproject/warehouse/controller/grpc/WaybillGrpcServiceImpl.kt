package com.ajaxproject.warehouse.controller.grpc

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Waybill.UPDATED_EVENT
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.waybill.GetWaybillUpdatesByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.waybill.GetWaybillUpdatesByIdResponse
import com.ajaxproject.api.internal.warehousesvc.service.product.ReactorWaybillGrpc
import com.ajaxproject.warehouse.service.WaybillService
import com.ajaxproject.warehouse.service.WaybillUpdatedNatsService
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toMono

@GrpcService
class WaybillGrpcServiceImpl(
    private val waybillService: WaybillService,
    private val waybillUpdatedNatsService: WaybillUpdatedNatsService
) : ReactorWaybillGrpc.WaybillImplBase() {

    override fun getWaybillUpdatesById(request: GetWaybillUpdatesByIdRequest): Flux<GetWaybillUpdatesByIdResponse> =
        request.toMono()    // TODO add failure handling
            .flatMap { waybillService.getById(it.id) }
            .map {
                GetWaybillUpdatesByIdResponse.newBuilder().apply {
                    waybill = it.mapToProto()
                }.build()
            }
            .concatWith(
                waybillUpdatedNatsService.subscribeToEvents(request.id, UPDATED_EVENT)
                    .map { waybillUpdatedEvent ->
                        GetWaybillUpdatesByIdResponse.newBuilder().apply {
                            waybill = waybillUpdatedEvent.waybill
                        }.build()
                    }
            )
}
