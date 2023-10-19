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
        reactiveMongoTemplate.findAll<MongoWaybill>(MongoWaybill.COLLECTION_NAME)

    override fun findById(id: ObjectId): Mono<MongoWaybill> =
        reactiveMongoTemplate.findById<MongoWaybill>(id, MongoWaybill.COLLECTION_NAME)

    override fun createWaybill(mongoWaybill: MongoWaybill): Mono<MongoWaybill> =
        reactiveMongoTemplate.insert(mongoWaybill, MongoWaybill.COLLECTION_NAME)

    override fun deleteById(id: ObjectId): Mono<Unit> =
        reactiveMongoTemplate.remove<MongoWaybill>(
            Query(Criteria.where("_id").`is`(id)),
            MongoWaybill.COLLECTION_NAME
        ).thenReturn(Unit)

    override fun save(mongoWaybill: MongoWaybill): Mono<MongoWaybill> =
        reactiveMongoTemplate.save(mongoWaybill, MongoWaybill.COLLECTION_NAME)
}
