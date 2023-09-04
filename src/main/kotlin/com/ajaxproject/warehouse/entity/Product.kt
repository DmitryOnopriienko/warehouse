package com.ajaxproject.warehouse.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var title: String,
    var price: Double,
    var amount: Int,
    var about: String?
)
