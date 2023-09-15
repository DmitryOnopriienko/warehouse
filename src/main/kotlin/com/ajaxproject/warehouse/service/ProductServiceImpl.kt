package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.annotation.RateLimit
import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductUpdateDto
import com.ajaxproject.warehouse.entity.Product
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(val productRepository: ProductRepository) : ProductService {
    override fun findAllProducts(): List<ProductDataLiteDto> {
        return productRepository.findAll().map { it.mapToLiteDto() }
    }

    @RateLimit
    override fun findById(id: Int): ProductDataDto {
        return productRepository.findById(id)
            .orElseThrow { NotFoundException("Product with id $id not found") }
            .mapToDataDto()
    }

    override fun createProduct(createDto: ProductCreateDto): ProductDataDto {
        val product = productRepository.save(createDto.mapToEntity())
        return product.mapToDataDto()
    }

    override fun updateProduct(updateDto: ProductUpdateDto, id: Int): ProductDataDto {
        require(id == updateDto.id) { "Mapping id is not equal to request body id" }
        val product: Product = productRepository.findById(id)
            .orElseThrow { NotFoundException("Product with id $id not found") }
        product.setUpdatedData(updateDto)
        productRepository.save(product)
        return product.mapToDataDto()
    }

    override fun deleteById(id: Int) {
        productRepository.deleteById(id)
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

    private fun Product.setUpdatedData(updateDto: ProductUpdateDto) {
        title = updateDto.title as String
        price = updateDto.price as Double
        amount = updateDto.amount as Int
        about = updateDto.about
    }

    companion object {
        private const val TIMEOUT: Long = 10000L

        private const val REPEAT: Int = 100
    }
}
