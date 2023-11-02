package com.ajaxproject.warehouse.service

import com.ajaxproject.api.internal.warehousesvc.KafkaTopic.Waybill.CREATION
import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillCreatedEvent
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val waybillCreatedEventKafkaTemplate: ReactiveKafkaProducerTemplate<String, WaybillCreatedEvent>,
) {
    fun sendWaybillCreationEvent(waybillCreatedEvent: WaybillCreatedEvent) {
        waybillCreatedEventKafkaTemplate.send(CREATION, waybillCreatedEvent).subscribe()
    }
}
