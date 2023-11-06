package com.ajaxproject.api.internal.warehousesvc

object NatsSubject {

    private const val WAREHOUSE_PREFIX = "com.ajaxproject.warehouse_svc"

    object Product {

        private const val PRODUCT_PREFIX = "$WAREHOUSE_PREFIX.product"

        const val FIND_ALL = "$PRODUCT_PREFIX.find_all"

        const val GET_BY_ID = "$PRODUCT_PREFIX.get_by_id"

        const val CREATE = "$PRODUCT_PREFIX.create"

        const val UPDATE = "$PRODUCT_PREFIX.update"

        const val DELETE = "$PRODUCT_PREFIX.delete"
    }

}
