package com.ajaxproject.warehouse.controller.kafka

import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillUpdatedEvent
import com.ajaxproject.warehouse.service.WaybillUpdatedNatsService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import reactor.kafka.receiver.KafkaReceiver

@Component
class WaybillUpdatedKafkaReceiver(
    private val waybillUpdatedNatsService: WaybillUpdatedNatsService,
    private val kafkaReceiver: KafkaReceiver<String, WaybillUpdatedEvent>
) {
    @PostConstruct
    fun init() {
        kafkaReceiver.receiveAutoAck()
            .flatMap { fluxRecord ->
                fluxRecord.map { waybillUpdatedNatsService.publishEvent(it.value()) }
            }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe()
    }
}
