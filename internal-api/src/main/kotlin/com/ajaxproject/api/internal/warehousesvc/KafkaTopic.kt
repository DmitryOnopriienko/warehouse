package com.ajaxproject.api.internal.warehousesvc

import com.ajaxproject.api.internal.warehousesvc.NatsSubject.WAREHOUSE_PREFIX

object KafkaTopic {

    object Waybill {

        private const val WAYBILL_PREFIX = "${WAREHOUSE_PREFIX}.waybill"

        const val CREATION = "$WAYBILL_PREFIX.creation"
    }
}
