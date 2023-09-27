package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class MongoWaybillRepositoryImpl(
    val mongoTemplate: MongoTemplate
) : MongoWaybillRepository {

    override fun findAll(): List<MongoWaybill> =
        mongoTemplate.findAll(MongoWaybill::class.java, MongoWaybill.COLLECTION_NAME)

    override fun getById(id: ObjectId): MongoWaybill? =
        mongoTemplate.findById(id, MongoWaybill::class.java, MongoWaybill.COLLECTION_NAME)

    override fun createWaybill(mongoWaybill: MongoWaybill): MongoWaybill {
        return mongoTemplate.insert(mongoWaybill, MongoWaybill.COLLECTION_NAME)
    }

    override fun deleteById(id: ObjectId) {
        mongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(id)),
            MongoWaybill::class.java,
            MongoWaybill.COLLECTION_NAME
        )
    }

    override fun save(mongoWaybill: MongoWaybill): MongoWaybill {
        return mongoTemplate.save(mongoWaybill, MongoWaybill.COLLECTION_NAME)
    }
}
