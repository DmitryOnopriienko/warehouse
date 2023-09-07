package com.ajaxproject.warehouse.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "waybill_has_products")
class WaybillProduct(

    @EmbeddedId
    var id: WaybillProductPK,

    @ManyToOne
    @JoinColumn(name = "waybill_id", insertable = false, updatable = false)
    @JsonBackReference
    var waybill: Waybill,

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    var product: Product,
    var amount: Int
) {

    @Embeddable
    data class WaybillProductPK(
        @Column(name = "waybill_id")
        var waybillId: Int,

        @Column(name = "product_id")
        var productId: Int
    ) : Serializable
}
