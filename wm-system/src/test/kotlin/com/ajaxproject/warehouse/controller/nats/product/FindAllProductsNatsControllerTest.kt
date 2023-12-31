package com.ajaxproject.warehouse.controller.nats.product

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.Product.FIND_ALL
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.infrastructure.mapper.mapToProto
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration

@SpringBootTest
@ActiveProfiles("local")
class FindAllProductsNatsControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var productService: ProductServiceInPort

    @Test
    fun testReturnsAllProducts() { // TODO another DB in another profile
        // GIVEN
        val expectedProtoProducts = productService.findAllProducts()
            .collectList()
            .block()!!
            .map { it.mapToProto() }
        val expectedProducts = FindAllProductsResponse.newBuilder().apply {
            successBuilder.productsBuilder
                .addAllProduct(expectedProtoProducts)
        }.build()

        // WHEN
        val response = connection.requestWithTimeout(
            FIND_ALL,
            FindAllProductsRequest.getDefaultInstance().toByteArray(),
            Duration.ofSeconds(10L)
        )

        // THEN
        val actualProducts = FindAllProductsResponse.parseFrom(response.get().data)
        assertEquals(expectedProducts, actualProducts)
    }
}
