package com.ajaxproject.warehouse.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "customer")
class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @Column(name = "first_name")
    var firstName: String,
    var surname: String,
    var patronymic: String?,
    var email: String,

    @Column(name = "phone_number")
    var phoneNumber: String?,
    var birthday: LocalDate?,
    var comment: String?
)
