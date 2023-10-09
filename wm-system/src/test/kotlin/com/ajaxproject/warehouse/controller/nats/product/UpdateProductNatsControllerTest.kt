package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.UPDATE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.repository.MongoProductRepository
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
    lateinit var productRepository: MongoProductRepository

    @Test
    fun testUpdateDataWithValidRequest() {

        val originalProduct = productRepository.createProduct(MongoProduct(
            title = "Original",
            price = 11.99,
            amount = 100,
            about = "original product"
        ))

        val expectedProduct = UpdateProductResponse.newBuilder().successBuilder
            .productBuilder
            .setId(originalProduct.id.toString())
            .setTitle("Updated title")
            .setPrice(9999.11)
            .setAmount(111)
            .setAbout("it is updated product")
            .build()

        val request = UpdateProductRequest.newBuilder().apply {
            id = originalProduct.id.toString()
            title = "Updated title"
            price = 9999.11
            amount = 111
            about = "it is updated product"
        }.build()

        val completableFuture = connection.requestWithTimeout(
            UPDATE,
            request.toByteArray(),
            Duration.ofSeconds(10L)
        )

        val actualResponse = UpdateProductResponse.parseFrom(completableFuture.get().data)

        assertTrue(actualResponse.hasSuccess())

        val actualProduct = actualResponse.success.product

        assertEquals(expectedProduct, actualProduct)
    }

    @Test
    fun testReturnsFailureOnInvalidRequest() {
        val originalProduct = productRepository.createProduct(MongoProduct(
            title = "Original",
            price = 11.99,
            amount = 100,
            about = "original product"
        ))

        val updateProductRequest = UpdateProductRequest.newBuilder()
            .setId(originalProduct.id.toString())
            .setTitle("Test create product")
            .setAmount(200)
            .setAbout("New test product")
            .build()

        val expectedResponse = UpdateProductResponse.newBuilder().apply {
            failureBuilder.setMessage(
                "Exception encountered: jakarta.validation.ConstraintViolationException: " +
                    "updateProduct.updateDto.price: price must be more than 0.01")
        }.build()

        val completableFuture = connection.requestWithTimeout(
            UPDATE,
            updateProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )


        val actualResponse = UpdateProductResponse.parseFrom(completableFuture.get().data)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testReturnsFailureOnRequestWithoutId() {
        val updateProductRequest = UpdateProductRequest.newBuilder()
            .setTitle("Test create product")
            .setPrice(19.99)
            .setAmount(200)
            .setAbout("New test product")
            .build()

        val expectedResponse = UpdateProductResponse.newBuilder().apply {
            failureBuilder.setMessage(
                "Exception encountered: java.lang.IllegalArgumentException: id must be provided")
        }.build()

        val completableFuture = connection.requestWithTimeout(
            UPDATE,
            updateProductRequest.toByteArray(),
            Duration.ofSeconds(10L)
        )

        val actualResponse = UpdateProductResponse.parseFrom(completableFuture.get().data)
        assertEquals(expectedResponse, actualResponse)
    }
}
