package com.ajaxproject.warehouse.service

import com.ajaxproject.api.internal.warehousesvc.KafkaTopic.Waybill.CREATED
import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillCreatedEvent
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class KafkaProducerService(
    private val waybillCreatedEventKafkaTemplate: ReactiveKafkaProducerTemplate<String, WaybillCreatedEvent>,
) {
    fun sendWaybillCreationEvent(waybillCreatedEvent: WaybillCreatedEvent): Mono<Unit> =
        Mono.fromSupplier {
            waybillCreatedEvent
        }
        .flatMap {
            waybillCreatedEventKafkaTemplate.send(CREATED, waybillCreatedEvent)
        }
        .thenReturn(Unit)
}
