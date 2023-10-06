package com.ajaxproject.warehouse.controller.nats

import io.nats.client.Connection
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class HelloWorldController(
    connection: Connection
) {

    init {
        connection.createDispatcher { msg ->
            val message = String(msg.data, StandardCharsets.UTF_8)
            println("Received message: $message")
            connection.publish("hi", msg.replyTo, "Hi $message".toByteArray())
        }.subscribe("hw")
    }
}
