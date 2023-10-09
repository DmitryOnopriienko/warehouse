package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.CREATE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun testCreatesProductFromValidRequest() {
        // GIVEN
        val createProductRequest = CreateProductRequest.newBuilder()
            .setTitle("Test create product")
            .setPrice(19.99)
            .setAmount(200)
            .setAbout("New test product")
            .build()

        val expectedProduct = CreateProductResponse.newBuilder()
            .successBuilder
                .productBuilder
                .setTitle("Test create product")
                .setPrice(19.99)
                .setAmount(200)
                .setAbout("New test product")
                .build()

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            CREATE,
            createProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )
        val actualResponse = CreateProductResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertTrue(actualResponse.hasSuccess())
        val actualProduct = actualResponse.success.product
        assertEquals(expectedProduct.title, actualProduct.title)
        assertEquals(expectedProduct.price, actualProduct.price)
        assertEquals(expectedProduct.amount, actualProduct.amount)
        assertEquals(expectedProduct.about, actualProduct.about)
    }

    @Test
    fun testReturnsFailureOnInvalidRequest() {
        // GIVEN
        val createProductRequest = CreateProductRequest.newBuilder()
            .setPrice(199.99)
            .setAmount(200)
            .setAbout("New test product")
            .build()

        val expectedResponse = CreateProductResponse.newBuilder().apply {
            failureBuilder.setMessage("Exception encountered: jakarta.validation.ConstraintViolationException: " +
                    "createProduct.createDto.title: title must be provided")
        }.build()

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            CREATE,
            createProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )
        val actualResponse = CreateProductResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertEquals(expectedResponse, actualResponse)
    }
}
