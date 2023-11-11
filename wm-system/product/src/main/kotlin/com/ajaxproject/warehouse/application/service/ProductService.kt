package com.ajaxproject.warehouse.application.service

import com.ajaxproject.warehouse.application.port.ProductRepositoryOutPort
import com.ajaxproject.warehouse.application.port.ProductServiceInPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.exception.NotFoundException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
    @Qualifier("redisProductRepository") private val productRepositoryOutPort: ProductRepositoryOutPort
) : ProductServiceInPort {

    override fun findAllProducts(): Flux<Product> =
        productRepositoryOutPort.findAll()

    override fun getById(id: String): Mono<Product> =
        productRepositoryOutPort.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Product with id $id not found")))

    override fun createProduct(productToCreate: Product): Mono<Product> =
        productRepositoryOutPort.createProduct(productToCreate)

    override fun updateProduct(productToUpdate: Product, id: String): Mono<Product> =
        productRepositoryOutPort.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Product with id $id not found")))
            .map { it.setUpdatedData(productToUpdate) }
            .flatMap { productRepositoryOutPort.save(it) }

    override fun deleteById(id: String): Mono<Unit> =
        productRepositoryOutPort.deleteById(id)

    private fun Product.setUpdatedData(productToUpdate: Product): Product {
        return this.copy(
            title = productToUpdate.title,
            price = productToUpdate.price,
            amount = productToUpdate.amount,
            about = productToUpdate.about
        )
    }
}
