package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.entity.Product
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(val productRepository: ProductRepository) : ProductService {
    override fun findAllProducts(): List<ProductDataLiteDto> {
        return productRepository.findAll().map { it.mapToLiteDto() }
    }

    override fun findById(id: Int): ProductDataDto {
        return productRepository.findById(id)
            .orElseThrow { NotFoundException("Product with id $id not found") }
            .mapToDataDto()
    }

    override fun createProduct(createDto: ProductCreateDto): ProductDataDto {
        val product = productRepository.save(createDto.mapToEntity())
        return product.mapToDataDto()
    }

    fun Product.mapToLiteDto(): ProductDataLiteDto = ProductDataLiteDto(
        id = id,
        title = title,
        price = price,
        amount = amount
    )

    fun Product.mapToDataDto(): ProductDataDto = ProductDataDto(
        id = id,
        title = title,
        price = price,
        amount = amount,
        about = about
    )

    fun ProductCreateDto.mapToEntity(): Product = Product(
        id = null,
        title = title,
        price = price,
        amount = amount,
        about = about
    )
}
