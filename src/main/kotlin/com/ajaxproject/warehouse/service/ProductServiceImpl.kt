package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductUpdateDto
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.MongoProductRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    val mongoProductRepository: MongoProductRepository
) : ProductService {

    override fun findAllProducts(): List<ProductDataLiteDto> =
        mongoProductRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): ProductDataDto {
        val mongoProduct: MongoProduct = mongoProductRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Product with id $id not found")
        return mongoProduct.mapToDataDto()
    }

    override fun createProduct(createDto: ProductCreateDto): ProductDataDto {
        val product: MongoProduct = mongoProductRepository.createProduct(createDto.mapToEntity())
        return product.mapToDataDto()
    }

    override fun updateProduct(updateDto: ProductUpdateDto, id: String): ProductDataDto {
        val product: MongoProduct = mongoProductRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Product with id $id not found")
        val updatedProduct = product.setUpdatedData(updateDto)
        return mongoProductRepository.save(updatedProduct).mapToDataDto()
    }

    override fun deleteById(id: String) {
        mongoProductRepository.deleteById(ObjectId(id))
    }

    fun MongoProduct.mapToLiteDto(): ProductDataLiteDto = ProductDataLiteDto(
        id = id.toString(),
        title = title,
        price = price,
        amount = amount
    )

    fun MongoProduct.mapToDataDto(): ProductDataDto = ProductDataDto(
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

    fun MongoProduct.setUpdatedData(updateDto: ProductUpdateDto): MongoProduct {
        return this.copy(
            title = updateDto.title as String,
            price = updateDto.price as Double,
            amount = updateDto.amount as Int,
            about = updateDto.about
        )
    }
}
