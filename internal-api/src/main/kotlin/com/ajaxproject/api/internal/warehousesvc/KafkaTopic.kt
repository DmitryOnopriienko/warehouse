package com.ajaxproject.api.internal.warehousesvc

object KafkaTopic {

    private const val WAREHOUSE_PREFIX = "com.ajaxproject.warehouse_svc"

    object Waybill {

        private const val WAYBILL_PREFIX = "${WAREHOUSE_PREFIX}.waybill"

        const val CREATED = "$WAYBILL_PREFIX.created"

        const val UPDATED = "$WAYBILL_PREFIX.updated"
    }
}
