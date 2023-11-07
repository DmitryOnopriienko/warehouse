package com.ajaxproject.warehouse.config

import com.ajaxproject.api.internal.warehousesvc.KafkaTopic.Waybill.CREATED
import com.ajaxproject.api.internal.warehousesvc.KafkaTopic.Waybill.UPDATED
import com.ajaxproject.api.internal.warehousesvc.output.pubsub.waybill.WaybillUpdatedEvent
import com.google.protobuf.GeneratedMessageV3
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    val bootstrapServers: String,
    @Value("\${spring.kafka.producer.key-serializer}")
    val keySerializer: String,
    @Value("\${spring.kafka.producer.value-serializer}")
    val valueSerializer: String,
    @Value("\${spring.kafka.consumer.key-deserializer}")
    val keyDeserializer: String,
    @Value("\${spring.kafka.consumer.value-deserializer}")
    val valueDeserializer: String,
    @Value("\${spring.kafka.registry.url}")
    val registryUrl: String
) {
    @Bean
    fun waybillCreationTopic(): NewTopic =
        TopicBuilder
            .name(CREATED)
            .build()

    @Bean
    fun waybillUpdatedTopic(): NewTopic =
        TopicBuilder
            .name(UPDATED)
            .build()

    @Bean
    fun <T: GeneratedMessageV3> senderOptions(): SenderOptions<String, T> {
        val props: Map<String, Any> = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to keySerializer,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to valueSerializer,
            "schema.registry.url" to registryUrl
        )
        return SenderOptions.create(props)
    }

    @Bean
    fun <T: GeneratedMessageV3> receiverOptions(customerProps: Map<String, Any> = mapOf()): ReceiverOptions<String, T> {
        val props: MutableMap<String, Any> = mutableMapOf(
            ConsumerConfig.GROUP_ID_CONFIG to "warehouse",
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to keyDeserializer,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to valueDeserializer,
            "schema.registry.url" to registryUrl
        )
        props.putAll(customerProps)
        return ReceiverOptions.create<String, T>(props).subscription(setOf(UPDATED))
    }

    @Bean
    fun <T: GeneratedMessageV3> kafkaSender(): KafkaSender<String, T> = KafkaSender.create(senderOptions())

    @Bean
    fun waybillUpdatedEventKafkaReceiver(): KafkaReceiver<String, WaybillUpdatedEvent> =
        KafkaReceiver.create(
            receiverOptions(
                mapOf(
                    KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE to WaybillUpdatedEvent::class.java.name
                )
            )
        )

    @Bean
    fun <T: GeneratedMessageV3> reactiveKafkaProducerTemplate(): ReactiveKafkaProducerTemplate<String, T> =
        ReactiveKafkaProducerTemplate(kafkaSender())
}
