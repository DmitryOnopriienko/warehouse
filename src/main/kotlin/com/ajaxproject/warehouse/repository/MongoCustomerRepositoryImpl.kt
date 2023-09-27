package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class MongoCustomerRepositoryImpl(
    val mongoTemplate: MongoTemplate
) : MongoCustomerRepository {

    override fun findAll(): List<MongoCustomer> = mongoTemplate.findAll<MongoCustomer>(MongoCustomer.COLLECTION_NAME)

    override fun findById(id: ObjectId): MongoCustomer? =
        mongoTemplate.findById<MongoCustomer>(id, MongoCustomer.COLLECTION_NAME)

    override fun createCustomer(mongoCustomer: MongoCustomer): MongoCustomer =
        mongoTemplate.insert(mongoCustomer, MongoCustomer.COLLECTION_NAME)

    override fun save(mongoCustomer: MongoCustomer): MongoCustomer =
        mongoTemplate.save(mongoCustomer, MongoCustomer.COLLECTION_NAME)

    override fun deleteById(id: ObjectId) {
        mongoTemplate.findAndRemove<MongoCustomer>(
            Query(Criteria.where("_id").`is`(id)),
            MongoCustomer.COLLECTION_NAME
        )
    }

    override fun findCustomerWaybills(id: ObjectId): List<MongoWaybill> =
        mongoTemplate.find<MongoWaybill>(
            Query(Criteria.where("customerId").`is`(id)),
            MongoWaybill.COLLECTION_NAME
        )
}
