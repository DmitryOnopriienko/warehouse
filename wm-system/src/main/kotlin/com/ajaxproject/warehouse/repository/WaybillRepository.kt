package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId

interface WaybillRepository {

    fun findAll(): List<MongoWaybill>

    fun findById(id: ObjectId): MongoWaybill?

    fun createWaybill(mongoWaybill: MongoWaybill): MongoWaybill

    fun deleteById(id: ObjectId)

    fun save(mongoWaybill: MongoWaybill): MongoWaybill
}
