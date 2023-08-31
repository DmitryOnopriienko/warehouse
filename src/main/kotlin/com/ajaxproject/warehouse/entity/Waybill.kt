package com.ajaxproject.warehouse.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.Date

@Entity
@Table(name = "waybill")
class Waybill(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @ManyToOne
//    @JsonBackReference
    @JoinColumn(name = "customer_id")
    var customer: Customer,
    var date: Date,
)
//    TODO implement total price (maybe on service layer)
