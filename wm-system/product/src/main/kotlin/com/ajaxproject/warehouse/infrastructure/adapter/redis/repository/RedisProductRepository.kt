package com.ajaxproject.warehouse.infrastructure.adapter.redis.repository

import com.ajaxproject.warehouse.application.port.ProductRepositoryOutPort
import com.ajaxproject.warehouse.domain.Product
import com.ajaxproject.warehouse.infrastructure.adapter.mongo.repository.MongoProductRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

@Repository
class RedisProductRepository(
    private val mongoProductRepository: MongoProductRepository,
    private val redisTemplate: ReactiveRedisTemplate<String, Product>,
    @Value("\${redis.ttl}")
    private val ttl: Long
) : ProductRepositoryOutPort by mongoProductRepository {

    override fun findById(id: String): Mono<Product> =
        redisTemplate.opsForValue()
            .get(id)
            .switchIfEmpty {
                mongoProductRepository.findById(id)
                    .flatMap { saveToCache(it) }
            }

    override fun createProduct(product: Product): Mono<Product> =
        mongoProductRepository.createProduct(product)
            .flatMap { saveToCache(it) }

    override fun save(product: Product): Mono<Product> =
        mongoProductRepository.save(product)
            .flatMap { saveToCache(it) }

    override fun deleteById(id: String): Mono<Unit> =
        mongoProductRepository.deleteById(id)
            .flatMap { redisTemplate.opsForValue().delete(id) }
            .thenReturn(Unit)

    private fun saveToCache(product: Product): Mono<Product> =
        redisTemplate.opsForValue()
            .set(product.id.toString(), product, Duration.ofMinutes(ttl))
            .thenReturn(product)
}
