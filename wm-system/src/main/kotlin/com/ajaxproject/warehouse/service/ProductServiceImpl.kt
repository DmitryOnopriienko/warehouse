package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductSaveDto
import com.ajaxproject.warehouse.entity.MongoProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.ProductCacheableRepository
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
    val productCacheableRepository: ProductCacheableRepository
) : ProductService {

    override fun findAllProducts(): Flux<ProductDataLiteDto> =
        productCacheableRepository.findAll().map { it.mapToLiteDto() }

    override fun getById(id: String): Mono<ProductDataDto> =
        productCacheableRepository.findById(ObjectId(id))
            .switchIfEmpty(Mono.error(NotFoundException("Product with id $id not found")))
            .map { it.mapToDataDto() }

    override fun createProduct(@Valid createDto: ProductSaveDto): Mono<ProductDataDto> =
        productCacheableRepository.createProduct(createDto.mapToEntity())
            .map { it.mapToDataDto() }

    @Transactional
    override fun updateProduct(@Valid updateDto: ProductSaveDto, id: String): Mono<ProductDataDto> =
        productCacheableRepository.findById(ObjectId(id))
            .switchIfEmpty(Mono.error(NotFoundException("Product with id $id not found")))
            .map { it.setUpdatedData(updateDto) }
            .flatMap {
                productCacheableRepository.save(it)
            }
            .map { it.mapToDataDto() }

    override fun deleteById(id: String): Mono<Unit> =
        productCacheableRepository.deleteById(ObjectId(id))

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
