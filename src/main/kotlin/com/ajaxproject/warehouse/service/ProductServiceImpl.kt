package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.entity.Product
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(val productRepository: ProductRepository) : ProductService {
    override fun findAll() : List<Product> {
        return productRepository.findAll()
    }

    override fun findById(id: Int) : Product {
        return productRepository.findById(id).orElseThrow { NotFoundException("Product with id $id not found") }
    }
}
