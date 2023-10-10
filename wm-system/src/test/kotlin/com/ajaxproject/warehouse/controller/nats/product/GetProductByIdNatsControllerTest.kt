package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.GET_BY_ID
import com.ajaxproject.api.internal.warehousesvc.commonmodels.product.Product
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.repository.MongoProductRepository
import io.nats.client.Connection
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration

@SpringBootTest
@ActiveProfiles("local")
class GetProductByIdNatsControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var productRepository: MongoProductRepository

    @Test
    fun testGetByIdSuccessReturnsValid() {
        // GIVEN
        val savedProduct = productRepository.createProduct(
            MongoProduct(
                title = "Test Product",
                price = 1.99,
                amount = 10,
                about = "Nothing to say"
            )
        )
        val expectedProduct = GetProductByIdResponse.newBuilder().apply {
            successBuilder.setProduct(
                Product.newBuilder().apply {
                    id = savedProduct.id.toString()
                    title = savedProduct.title
                    price = savedProduct.price
                    amount = savedProduct.amount
                    about = savedProduct.about
                }
            )
        }.build()

        /// WHEN
        val completableFuture = connection.requestWithTimeout(
            GET_BY_ID,
            GetProductByIdRequest.newBuilder().apply {
                id = savedProduct.id.toString()
            }
                .build()
                .toByteArray(),
            Duration.ofSeconds(10L)
        )
        val actualProduct = GetProductByIdResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertEquals(expectedProduct, actualProduct)
    }

    @Test
    fun testGetByIdFailureReturnsException() {
        // GIVEN
        val savedProduct = productRepository.createProduct(
            MongoProduct(
                title = "Test Product",
                price = 1.99,
                amount = 10,
                about = "Nothing to say"
            )
        )
        val idOfDeleted = savedProduct.id.toString()
        productRepository.deleteById(ObjectId(idOfDeleted))

        val expectedResponse = GetProductByIdResponse.newBuilder().apply {
            failureBuilder
                .setMessage("Exception encountered: com.ajaxproject.warehouse.exception.NotFoundException: " +
                        "Product with id $idOfDeleted not found")
        }.build()

        // WHEN
        val completableFuture = connection.requestWithTimeout(
            GET_BY_ID,
            GetProductByIdRequest.newBuilder().apply {
                id = idOfDeleted
            }
                .build()
                .toByteArray(),
            Duration.ofSeconds(10L)
        )
        val response = GetProductByIdResponse.parseFrom(completableFuture.get().data)

        // THEN
        assertEquals(expectedResponse, response)
    }
}
