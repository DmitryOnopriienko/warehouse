package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductDataLiteDto
import com.ajaxproject.warehouse.dto.mongo.MongoProductUpdateDto
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.mongo.MongoProductRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ProductServiceMongoImpl(
    val mongoProductRepository: MongoProductRepository
) : ProductServiceMongo {
    override fun findAll(): List<MongoProductDataLiteDto> {
        return mongoProductRepository.findAll().map { it.mapToLiteDto() }
    }

    override fun getById(id: String): MongoProductDataDto {
        val mongoProduct: MongoProduct = mongoProductRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Product with id $id not found")
        return mongoProduct.mapToDataDto()
    }

    override fun createProduct(createDto: ProductCreateDto): MongoProductDataDto {
        val product: MongoProduct = mongoProductRepository.createProduct(createDto.mapToEntity())
        return product.mapToDataDto()
    }

    override fun updateProduct(updateDto: MongoProductUpdateDto, id: String): MongoProductDataDto {
        require(id == updateDto.id) { "Mapping id is not equal to request body id" }
        var product: MongoProduct = mongoProductRepository.getById(ObjectId(id))
            ?: throw NotFoundException("Product with id $id not found")
        product = product.setUpdatedData(updateDto)
        product = mongoProductRepository.save(product)
        return product.mapToDataDto()
    }

    override fun deleteById(id: String) {
        mongoProductRepository.deleteById(ObjectId(id))
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