package com.ajaxproject.warehouse.nats

import io.nats.client.Connection
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class NatsController(
    connection: Connection,
) {

    val subj: String = "helloworld.get"

    init {
        connection.createDispatcher { msg ->
            val message = String(msg.data, StandardCharsets.UTF_8)
            println("Received message: $message")
        }.subscribe(subj)
    }
}
