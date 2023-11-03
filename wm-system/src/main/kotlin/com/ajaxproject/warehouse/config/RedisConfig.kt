package com.ajaxproject.warehouse.config

import com.ajaxproject.warehouse.entity.MongoProduct
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
    fun reactiveRedisTemplate(
        factory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, MongoProduct> {
        val keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val valueSerializer = Jackson2JsonRedisSerializer(objectMapper, MongoProduct::class.java)
        val context = RedisSerializationContext.newSerializationContext<String, MongoProduct>(keySerializer)
            .value(valueSerializer)
            .build()
        return ReactiveRedisTemplate(factory, context)
    }
}
