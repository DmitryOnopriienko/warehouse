package com.ajaxproject.warehouse.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "waybill_has_products")
class WaybillProduct(
    @Id
    @ManyToOne
    @JoinColumn(name = "waybill_id")
    var waybill: Waybill,
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,
    var amount: Int)