package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.CREATE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import com.ajaxproject.warehouse.repository.MongoProductRepository
import io.nats.client.Connection
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration

@SpringBootTest
@ActiveProfiles("local")
class CreateProductNatsControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var productRepository: MongoProductRepository

    @Test
    fun testCreatesProductFromValidRequest() {
        val createProductRequest = CreateProductRequest.newBuilder()
            .setTitle("Test product")
            .setPrice(19.99)
            .setAmount(200)
            .setAbout("New test product")
            .build()

        val completableFuture = connection.requestWithTimeout(
            CREATE,
            createProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )

        val createdProduct = CreateProductResponse.parseFrom(completableFuture.get().data)
        println("createdProduct = $createdProduct")
    }
}
