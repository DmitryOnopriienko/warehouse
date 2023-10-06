package com.ajaxproject.warehouse.beanpostprocessor

import com.ajaxproject.warehouse.controller.nats.NatsController
import com.google.protobuf.GeneratedMessageV3
import io.nats.client.Connection
import io.nats.client.Message
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class NatsControllerInitializationBeanPostProcessor(val connection: Connection) : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            initializeNatsController(bean)
        }
        return bean
    }

    private fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3> initializeNatsController(
        controller: NatsController<RequestT, ResponseT>
    ) {
        connection.createDispatcher { message: Message ->
            val parsedData = controller.parser.parseFrom(message.data)
            val response = controller.handle(parsedData)
            connection.publish(controller.subject, message.replyTo, response.toByteArray())
        }.apply { subscribe(controller.subject) }
    }
}
