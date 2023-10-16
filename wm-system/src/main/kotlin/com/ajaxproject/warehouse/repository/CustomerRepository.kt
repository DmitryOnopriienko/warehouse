package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerRepository {

    fun findAll(): List<MongoCustomer>

    fun findAllR(): Flux<MongoCustomer>

    fun findById(id: ObjectId): MongoCustomer?

    fun findByIdR(id: ObjectId): Mono<MongoCustomer>

    fun createCustomer(mongoCustomer: MongoCustomer): MongoCustomer

    fun createCustomerR(mongoCustomer: MongoCustomer): Mono<MongoCustomer>

    fun save(mongoCustomer: MongoCustomer): MongoCustomer

    fun saveR(mongoCustomer: MongoCustomer): Mono<MongoCustomer>

    fun deleteById(id: ObjectId)

    fun deleteByIdR(id: ObjectId)

    fun findCustomerWaybills(id: ObjectId): List<MongoWaybill>

    fun findCustomerWaybillsR(id: ObjectId): Flux<MongoWaybill>
}
