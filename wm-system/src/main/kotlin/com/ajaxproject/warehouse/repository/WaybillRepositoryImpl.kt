package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class WaybillRepositoryImpl(
    val mongoTemplate: MongoTemplate
) : WaybillRepository {

    override fun findAll(): List<MongoWaybill> =
        mongoTemplate.findAll<MongoWaybill>(MongoWaybill.COLLECTION_NAME)

    override fun findById(id: ObjectId): MongoWaybill? =
        mongoTemplate.findById<MongoWaybill>(id, MongoWaybill.COLLECTION_NAME)

    override fun createWaybill(mongoWaybill: MongoWaybill): MongoWaybill {
        return mongoTemplate.insert(mongoWaybill, MongoWaybill.COLLECTION_NAME)
    }

    override fun deleteById(id: ObjectId) {
        mongoTemplate.findAndRemove<MongoWaybill>(
            Query(Criteria.where("_id").`is`(id)),
            MongoWaybill.COLLECTION_NAME
        )
    }

    override fun save(mongoWaybill: MongoWaybill): MongoWaybill {
        return mongoTemplate.save(mongoWaybill, MongoWaybill.COLLECTION_NAME)
    }
}
