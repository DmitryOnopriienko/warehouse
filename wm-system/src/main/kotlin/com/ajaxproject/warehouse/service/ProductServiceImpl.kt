package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductSaveDto
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.ProductRepository
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Validated
class ProductServiceImpl(
    val productRepository: ProductRepository
) : ProductService {

    override fun findAllProducts(): List<ProductDataLiteDto> =
        productRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): ProductDataDto {
        val mongoProduct: MongoProduct = productRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Product with id $id not found")
        return mongoProduct.mapToDataDto()
    }

    override fun createProduct(@Valid createDto: ProductSaveDto): ProductDataDto {
        val product: MongoProduct = productRepository.createProduct(createDto.mapToEntity())
        return product.mapToDataDto()
    }

    @Transactional
    override fun updateProduct(@Valid updateDto: ProductSaveDto, id: String): ProductDataDto {
        val product: MongoProduct = productRepository.findById(ObjectId(id))
            ?: throw NotFoundException("Product with id $id not found")
        val updatedProduct = product.setUpdatedData(updateDto)
        return productRepository.save(updatedProduct).mapToDataDto()
    }

    override fun deleteById(id: String) {
        productRepository.deleteById(ObjectId(id))
    }

    override fun findAllProductsR(): Flux<ProductDataLiteDto> =
        productRepository.findAllR().map { it.mapToLiteDto() }

    override fun getByIdR(id: String): Mono<ProductDataDto> {
        val customer = productRepository.findByIdR(ObjectId(id))
            .switchIfEmpty(Mono.error(NotFoundException("Product with id $id not found")))
        return customer.map { it.mapToDataDto() }
    }

    override fun createProductR(@Valid createDto: ProductSaveDto): Mono<ProductDataDto> {
        val customer = productRepository.createProductR(createDto.mapToEntity())
        return customer.map { it.mapToDataDto() }
    }

    @Transactional
    override fun updateProductR(@Valid updateDto: ProductSaveDto, id: String): Mono<ProductDataDto> {
        val product = productRepository.findByIdR(ObjectId(id))
            .switchIfEmpty(Mono.error(NotFoundException("Product with id $id not found")))
        return product
            .map { it.setUpdatedData(updateDto) }
            .flatMap {
                productRepository.saveR(it)
            }
            .map { it.mapToDataDto() }
    }

    override fun deleteByIdR(id: String): Mono<Unit> =
        productRepository.deleteByIdR(ObjectId(id))

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

    fun ProductSaveDto.mapToEntity(): MongoProduct = MongoProduct(
        title = title as String,
        price = price as Double,
        amount = amount as Int,
        about = about
    )

    fun MongoProduct.setUpdatedData(updateDto: ProductSaveDto): MongoProduct {
        return this.copy(
            title = updateDto.title as String,
            price = updateDto.price as Double,
            amount = updateDto.amount as Int,
            about = updateDto.about
        )
    }
}
