package com.ajaxproject.warehouse.repository

import com.ajaxproject.warehouse.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Int>
