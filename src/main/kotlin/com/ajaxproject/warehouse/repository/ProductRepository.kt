package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Int>