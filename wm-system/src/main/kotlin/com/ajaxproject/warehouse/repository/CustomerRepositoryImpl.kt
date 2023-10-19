package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CustomerRepositoryImpl(
    val mongoTemplate: MongoTemplate,
    val reactiveMongoTemplate: ReactiveMongoTemplate
) : CustomerRepository {

    override fun findAll(): Flux<MongoCustomer> = reactiveMongoTemplate.findAll<MongoCustomer>(MongoCustomer.COLLECTION_NAME)

    override fun findByIdReactive(id: ObjectId): Mono<MongoCustomer> =
        reactiveMongoTemplate.findById<MongoCustomer>(id, MongoCustomer.COLLECTION_NAME)

    override fun findById(id: ObjectId): MongoCustomer? =
        mongoTemplate.findById<MongoCustomer>(id, MongoCustomer.COLLECTION_NAME)

    override fun createCustomer(mongoCustomer: MongoCustomer): Mono<MongoCustomer> =
        reactiveMongoTemplate.insert(mongoCustomer, MongoCustomer.COLLECTION_NAME)

    override fun save(mongoCustomer: MongoCustomer): Mono<MongoCustomer> =
        reactiveMongoTemplate.save(mongoCustomer, MongoCustomer.COLLECTION_NAME)

    override fun deleteById(id: ObjectId): Mono<Unit> =
        reactiveMongoTemplate.remove<MongoCustomer>(
            Query(Criteria.where("_id").`is`(id)),
            MongoCustomer.COLLECTION_NAME
        ).thenReturn(Unit)

    override fun findCustomerWaybillsReactive(id: ObjectId): Flux<MongoWaybill> =
        reactiveMongoTemplate.find<MongoWaybill>(
            Query(Criteria.where("customerId").`is`(id)),
            MongoWaybill.COLLECTION_NAME
        )

    override fun findCustomerWaybills(id: ObjectId): List<MongoWaybill> =
        mongoTemplate.find<MongoWaybill>(
            Query(Criteria.where("customerId").`is`(id)),
            MongoWaybill.COLLECTION_NAME
        )
}
