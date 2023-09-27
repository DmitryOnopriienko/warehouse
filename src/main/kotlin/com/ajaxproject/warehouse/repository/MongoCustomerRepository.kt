package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.MongoCustomer
import com.ajaxproject.warehouse.entity.MongoWaybill
import org.bson.types.ObjectId

interface MongoCustomerRepository {
    fun findAll(): List<MongoCustomer>
    fun getById(id: ObjectId): MongoCustomer?
    fun createCustomer(mongoCustomer: MongoCustomer): MongoCustomer
    fun save(mongoCustomer: MongoCustomer): MongoCustomer
    fun deleteById(id: ObjectId)
    fun findCustomerWaybills(id: ObjectId?): List<MongoWaybill>
}
