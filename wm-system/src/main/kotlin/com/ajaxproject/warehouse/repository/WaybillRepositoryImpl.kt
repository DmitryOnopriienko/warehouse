package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class WaybillRepositoryImpl(
    val reactiveMongoTemplate: ReactiveMongoTemplate
) : WaybillRepository {

    override fun findAll(): Flux<MongoWaybill> =
        reactiveMongoTemplate.findAll<MongoWaybill>()

    override fun findById(id: ObjectId): Mono<MongoWaybill> =
        reactiveMongoTemplate.findById<MongoWaybill>(id)

    override fun createWaybill(mongoWaybill: MongoWaybill): Mono<MongoWaybill> =
        reactiveMongoTemplate.insert(mongoWaybill)

    override fun deleteById(id: ObjectId): Mono<Unit> =
        reactiveMongoTemplate.remove<MongoWaybill>(
            Query(Criteria.where("_id").`is`(id))
        ).thenReturn(Unit)

    override fun save(mongoWaybill: MongoWaybill): Mono<MongoWaybill> =
        reactiveMongoTemplate.save(mongoWaybill)
}
