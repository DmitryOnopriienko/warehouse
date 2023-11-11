package com.ajaxproject.warehouse.infrastructure.adapter.redis.config

import com.ajaxproject.warehouse.domain.Product
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun reactiveRedisProductTemplate(
        factory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, Product> {
        val keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val valueSerializer = Jackson2JsonRedisSerializer(objectMapper, Product::class.java)
        val context = RedisSerializationContext.newSerializationContext<String, Product>(keySerializer)
            .value(valueSerializer)
            .build()
        return ReactiveRedisTemplate(factory, context)
    }
}
