package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface WaybillRepository {

    fun findAll(): List<MongoWaybill>

    fun findById(id: ObjectId): MongoWaybill?

    fun createWaybill(mongoWaybill: MongoWaybill): MongoWaybill

    fun deleteById(id: ObjectId)

    fun save(mongoWaybill: MongoWaybill): MongoWaybill

    fun findAllR(): Flux<MongoWaybill>

    fun findByIdR(id: ObjectId): Mono<MongoWaybill>

    fun createWaybillR(mongoWaybill: MongoWaybill): Mono<MongoWaybill>

    fun deleteByIdR(id: ObjectId): Mono<Unit>

    fun saveR(mongoWaybill: MongoWaybill): Mono<MongoWaybill>
}
