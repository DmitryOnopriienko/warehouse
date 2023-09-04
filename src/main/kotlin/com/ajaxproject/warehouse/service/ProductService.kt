package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.entity.Product

interface ProductService {
    fun findAll() : List<Product>
    fun findById(id: Int) : Product
}
