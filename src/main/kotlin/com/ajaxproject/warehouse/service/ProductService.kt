package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.ProductCreateDto
import com.ajaxproject.warehouse.dto.ProductDataDto
import com.ajaxproject.warehouse.dto.ProductDataLiteDto
import com.ajaxproject.warehouse.dto.ProductUpdateDto

interface ProductService {
    fun findAllProducts() : List<ProductDataLiteDto>
    fun findById(id: Int) : ProductDataDto
    fun createProduct(createDto: ProductCreateDto): ProductDataDto
    fun deleteById(id: Int)
    fun updateProduct(updateDto: ProductUpdateDto, id: Int): ProductDataDto
}
