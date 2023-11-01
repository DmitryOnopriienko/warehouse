package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerRepository {

    fun findAll(): Flux<MongoCustomer>

    fun findById(id: ObjectId): Mono<MongoCustomer>

    fun createCustomer(mongoCustomer: MongoCustomer): Mono<MongoCustomer>

    fun save(mongoCustomer: MongoCustomer): Mono<MongoCustomer>

    fun deleteById(id: ObjectId): Mono<Unit>

    fun findCustomerWaybills(id: ObjectId): Flux<MongoWaybill>
}
