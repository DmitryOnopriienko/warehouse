package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto

interface ProductService {
    fun findAllProducts() : List<ProductDataLiteDto>
    fun findById(id: Int) : ProductDataDto
    fun createProduct(createDto: ProductCreateDto): ProductDataDto
}
