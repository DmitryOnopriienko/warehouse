package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.DELETE
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.repository.MongoProductRepository
import io.nats.client.Connection
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration

@SpringBootTest
@ActiveProfiles("local")
class DeleteProductByIdNatsControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var productRepository: MongoProductRepository

    @Test
    fun testDeleteWithValidId() {
        // GIVEN
        val originalProduct = productRepository.createProduct(
            MongoProduct(
                title = "Original",
                price = 11.99,
                amount = 100,
                about = "original product"
            )
        )

        assertNotNull(productRepository.findById(originalProduct.id as ObjectId))

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            DELETE,
            DeleteProductByIdRequest.newBuilder().apply {
                id = originalProduct.id.toString()
            }
                .build()
                .toByteArray(),
            Duration.ofSeconds(10L)
        )
        val actualResponse = DeleteProductByIdResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertTrue(actualResponse.hasSuccess())
        assertNull(productRepository.findById(originalProduct.id as ObjectId))
    }

    @Test
    fun testReturnsFailureWithInvalidId() {
        // GIVEN
        val originalProduct = productRepository.createProduct(
            MongoProduct(
                title = "Original",
                price = 11.99,
                amount = 100,
                about = "original product"
            )
        )

        assertNotNull(productRepository.findById(originalProduct.id as ObjectId))

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            DELETE,
            DeleteProductByIdRequest.getDefaultInstance().toByteArray(),
            Duration.ofSeconds(10L)
        )
        val actualResponse = DeleteProductByIdResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertTrue(actualResponse.hasFailure())
        assertNotNull(productRepository.findById(originalProduct.id as ObjectId))
    }
}
