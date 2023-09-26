package com.ajaxproject.warehouse.repository.mongo

import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class MongoCustomerRepositoryImpl(
    val mongoTemplate: MongoTemplate
) : MongoCustomerRepository {

    private val customerType = MongoCustomer::class.java

    private val waybillType = MongoWaybill::class.java

    override fun findAll(): List<MongoCustomer> = mongoTemplate.findAll(customerType, MongoCustomer.COLLECTION_NAME)

    override fun getById(id: ObjectId): MongoCustomer? =
        mongoTemplate.findById(id, customerType, MongoCustomer.COLLECTION_NAME)

    override fun createCustomer(mongoCustomer: MongoCustomer): MongoCustomer =
        mongoTemplate.insert(mongoCustomer, MongoCustomer.COLLECTION_NAME)

    override fun save(mongoCustomer: MongoCustomer): MongoCustomer =
        mongoTemplate.save(mongoCustomer, MongoCustomer.COLLECTION_NAME)

    override fun deleteById(id: ObjectId) {
        mongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(id)),
            customerType,
            MongoCustomer.COLLECTION_NAME
        )
    }

    override fun findCustomerWaybills(id: ObjectId?): List<MongoWaybill> =
        mongoTemplate.find(
            Query(Criteria.where("customer_id").`is`(id)),
            waybillType,
            MongoWaybill.COLLECTION_NAME
        )
}
