package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductUpdateDto
import jakarta.validation.Valid

interface ProductService {

    fun findAllProducts(): List<ProductDataLiteDto>

    fun getById(id: String): ProductDataDto

    fun createProduct(@Valid createDto: ProductCreateDto): ProductDataDto

    fun updateProduct(updateDto: ProductUpdateDto, id: String): ProductDataDto

    fun deleteById(id: String)
}
