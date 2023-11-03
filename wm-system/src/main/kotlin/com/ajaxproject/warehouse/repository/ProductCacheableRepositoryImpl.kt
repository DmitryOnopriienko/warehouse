package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoProduct
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

@Repository
class ProductCacheableRepositoryImpl(
    val productRepository: ProductRepository,
    val redisTemplate: ReactiveRedisTemplate<String, MongoProduct>,
    @Value("\${redis.ttl}")
    val ttl: Long
) : ProductRepository {

    override fun findById(id: ObjectId): Mono<MongoProduct> =
        redisTemplate.opsForValue()
            .get(id.toString())
            .switchIfEmpty {
                productRepository.findById(id)
                    .flatMap { product ->
                        redisTemplate.opsForValue()
                            .set(id.toString(), product, Duration.ofMinutes(ttl))
                            .thenReturn(product)
                    }
            }

    override fun createProduct(mongoProduct: MongoProduct): Mono<MongoProduct> =
        productRepository.createProduct(mongoProduct)
            .flatMap { product ->
                redisTemplate.opsForValue()
                    .set(product.id.toString(), product, Duration.ofMinutes(ttl))
                    .thenReturn(product)
            }

    override fun save(mongoProduct: MongoProduct): Mono<MongoProduct> =
        productRepository.save(mongoProduct)
            .flatMap { product ->
                redisTemplate.opsForValue()
                    .set(product.id.toString(), product, Duration.ofMinutes(ttl))
                    .thenReturn(product)
            }

    override fun deleteById(id: ObjectId): Mono<Unit> =
        productRepository.deleteById(id)
            .flatMap { redisTemplate.opsForValue().delete(id.toString()) }
            .thenReturn(Unit)

    override fun findAll(): Flux<MongoProduct> =
        productRepository.findAll()

    override fun findValidEntities(ids: List<ObjectId>): Flux<MongoProduct> =
        productRepository.findValidEntities(ids)
}
