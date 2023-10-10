package com.ajaxproject.warehouse.config

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NatsConfig(
    @Value("\${nats.url}")
    val connectionUrl: String
) {
    @Bean
    fun getNatsConnection(): Connection = Nats.connect(connectionUrl)

}
