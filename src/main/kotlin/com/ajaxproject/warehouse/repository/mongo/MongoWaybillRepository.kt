package com.ajaxproject.warehouse.repository.mongo

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId

interface MongoWaybillRepository {
    fun findAll(): List<MongoWaybill>
    fun getById(id: ObjectId): MongoWaybill?
    fun createWaybill(mongoWaybill: MongoWaybill): MongoWaybill
}
