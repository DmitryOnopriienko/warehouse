package com.ajaxproject.warehouse.config

import com.ajaxproject.api.internal.warehousesvc.KafkaTopic.Waybill.CREATED
import com.google.protobuf.GeneratedMessageV3
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
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
    @Value("\${spring.kafka.registry.url}")
    val registryUrl: String
) {
    @Bean
    fun waybillCreationTopic(): NewTopic =
        TopicBuilder
            .name(CREATED)
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
    fun <T: GeneratedMessageV3> kafkaSender(): KafkaSender<String, T> = KafkaSender.create(senderOptions())

    @Bean
    fun <T: GeneratedMessageV3> kafkaReactiveTemplate(): ReactiveKafkaProducerTemplate<String, T> =
        ReactiveKafkaProducerTemplate(kafkaSender())
}
