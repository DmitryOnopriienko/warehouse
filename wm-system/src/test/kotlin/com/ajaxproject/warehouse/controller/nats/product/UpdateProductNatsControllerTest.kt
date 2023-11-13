package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.UPDATE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.application.port.ProductRepositoryOutPort
import com.ajaxproject.warehouse.infrastructure.adapter.mongo.entity.MongoProduct
import com.ajaxproject.warehouse.infrastructure.mapper.mapToDomain
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
class UpdateProductNatsControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var productRepository: ProductRepositoryOutPort

    @Test
    fun testUpdateDataWithValidRequest() {
        // GIVEN
        val originalProduct = productRepository.createProduct(
            MongoProduct(
                title = "Original",
                price = 11.99,
                amount = 100,
                about = "original product"
            ).mapToDomain()
        ).block()!!

        val expectedProduct = UpdateProductResponse.newBuilder().successBuilder
            .productBuilder.apply {
                id = originalProduct.id.toString()
                title = "Updated title"
                price = 9999.11
                amount = 111
                about = "it is updated product"
            }.build()

        val request = UpdateProductRequest.newBuilder().apply {
            id = originalProduct.id.toString()
            title = "Updated title"
            price = 9999.11
            amount = 111
            about = "it is updated product"
        }.build()

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            UPDATE,
            request.toByteArray(),
            Duration.ofSeconds(10L)
        )
        val actualResponse = UpdateProductResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertTrue(actualResponse.hasSuccess())

        val actualProduct = actualResponse.success.product

        assertEquals(expectedProduct, actualProduct)
    }

    @Test
    fun testReturnsFailureOnInvalidRequest() {
        // GIVEN
        val originalProduct = productRepository.createProduct(
            MongoProduct(
                title = "Original",
                price = 11.99,
                amount = 100,
                about = "original product"
            ).mapToDomain()
        ).block()!!

        val updateProductRequest = UpdateProductRequest.newBuilder().apply {
            id = originalProduct.id.toString()
            title = "Test create product"
            amount = 200
            about = "New test product"
        }.build()

        val expectedResponse = UpdateProductResponse.newBuilder().apply {
            failureBuilder.setMessage(
                "Exception encountered: java.lang.IllegalArgumentException: price must be provided"
            )
        }.build()

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            UPDATE,
            updateProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )

        // THEN
        val actualResponse = UpdateProductResponse.parseFrom(completableFuture.get().data)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testReturnsFailureOnRequestWithoutId() {
        // GIVEN
        val updateProductRequest = UpdateProductRequest.newBuilder().apply {
            title = "Test create product"
            price = 19.99
            amount = 200
            about = "New test product"
        }.build()

        val expectedResponse = UpdateProductResponse.newBuilder().apply {
            failureBuilder.setMessage(
                "Exception encountered: java.lang.IllegalArgumentException: Product ID cannot be empty"
            )
        }.build()

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            UPDATE,
            updateProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )

        // THEN
        val actualResponse = UpdateProductResponse.parseFrom(completableFuture.get().data)
        assertEquals(expectedResponse, actualResponse)
    }
}
