package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductUpdateDto
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class ProductServiceMongoImpl(
    val mongoTemplate: MongoTemplate
) : ProductServiceMongo {
    override fun findAll(): List<MongoProductDataLiteDto> {
        return mongoTemplate.findAll(MongoProduct::class.java).map { it.mapToLiteDto() }
    }

    override fun getById(id: String): MongoProductDataDto {
        val mongoProduct = mongoTemplate.findById(ObjectId(id), MongoProduct::class.java)
            ?: throw NotFoundException("Product with id $id not found")
        return mongoProduct.mapToDataDto()
    }

    override fun createProduct(createDto: ProductCreateDto): MongoProductDataDto {
        val product = mongoTemplate.insert(createDto.mapToEntity(), MongoProduct.COLLECTION_NAME)
        return product.mapToDataDto()
    }

    override fun updateProduct(updateDto: MongoProductUpdateDto, id: String): MongoProductDataDto {
        require(id == updateDto.id) { "Mapping id is not equal to request body id" }
        var product: MongoProduct = mongoTemplate.findById(
            ObjectId(id),
            MongoProduct::class.java,
            MongoProduct.COLLECTION_NAME
        ) ?: throw NotFoundException("Product with id $id not found")

        product = product.setUpdatedData(updateDto)
        product = mongoTemplate.save(product, MongoProduct.COLLECTION_NAME)
        return product.mapToDataDto()
    }

    override fun deleteById(id: String) {
        mongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(ObjectId(id))),
            MongoProduct::class.java,
            MongoProduct.COLLECTION_NAME
        )
    }

    fun MongoProduct.mapToLiteDto(): MongoProductDataLiteDto = MongoProductDataLiteDto(
        id = id.toString(),
        title = title,
        price = price,
        amount = amount
    )

    fun MongoProduct.mapToDataDto(): MongoProductDataDto = MongoProductDataDto(
        id = id.toString(),
        title = title,
        price = price,
        amount = amount,
        about = about
    )

    fun ProductCreateDto.mapToEntity(): MongoProduct = MongoProduct(
        title = title as String,
        price = price as Double,
        amount = amount,
        about = about
    )

    fun MongoProduct.setUpdatedData(updateDto: MongoProductUpdateDto): MongoProduct {
        return this.copy(
            title = updateDto.title as String,
            price = updateDto.price as Double,
            amount = updateDto.amount as Int,
            about = updateDto.about
        )
    }

}