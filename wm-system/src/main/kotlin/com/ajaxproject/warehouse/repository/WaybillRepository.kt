package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface WaybillRepository {

    fun findAll(): Flux<MongoWaybill>

    fun findById(id: ObjectId): Mono<MongoWaybill>

    fun createWaybill(mongoWaybill: MongoWaybill): Mono<MongoWaybill>

    fun deleteById(id: ObjectId): Mono<Unit>

    fun save(mongoWaybill: MongoWaybill): Mono<MongoWaybill>
}
