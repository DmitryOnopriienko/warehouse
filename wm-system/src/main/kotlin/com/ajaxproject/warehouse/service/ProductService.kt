package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductSaveDto
import jakarta.validation.Valid

interface ProductService {

    fun findAllProducts(): List<ProductDataLiteDto>

    fun getById(id: String): ProductDataDto

    fun createProduct(@Valid createDto: ProductSaveDto): ProductDataDto

    fun updateProduct(@Valid updateDto: ProductSaveDto, id: String): ProductDataDto

    fun deleteById(id: String)
}
