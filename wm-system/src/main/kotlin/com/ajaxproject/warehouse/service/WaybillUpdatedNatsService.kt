package com.ajaxproject.warehouse.service

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Waybill.UPDATED_EVENT
import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Waybill.createWaybillEventSubject
import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillUpdatedEvent
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class WaybillUpdatedNatsService(
    private val connection: Connection
) {

    private val parser: Parser<WaybillUpdatedEvent> = WaybillUpdatedEvent.parser()

    private val dispatcher = connection.createDispatcher()

    fun subscribeToEvents(waybillId: String, eventType: String): Flux<WaybillUpdatedEvent> =
        Flux.create { sink ->
            dispatcher.apply {
                subscribe(createWaybillEventSubject(waybillId, eventType)) { message ->
                    val parsedData = parser.parseFrom(message.data)
                    sink.next(parsedData)
                }
            }
        }

    fun publishEvent(waybillUpdatedEvent: WaybillUpdatedEvent) {
        val updateEventSubject = createWaybillEventSubject(waybillUpdatedEvent.waybill.id, UPDATED_EVENT)
        connection.publish(updateEventSubject, waybillUpdatedEvent.toByteArray())
    }
}
