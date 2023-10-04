package com.ajaxproject.api.internal.warehouse

object NatsSubject {

    private const val WAREHOUSE_PREFIX = "com.ajaxproject.warehouse"

    object Customer {

        private const val CUSTOMER_PREFIX = "$WAREHOUSE_PREFIX.customer"

        const val FIND_ALL = "$CUSTOMER_PREFIX.find_all"

        const val GET_BY_ID = "$CUSTOMER_PREFIX.get_by_id"

        const val CREATE = "$CUSTOMER_PREFIX.create"

        const val UPDATE = "$CUSTOMER_PREFIX.update"

        const val DELETE = "$CUSTOMER_PREFIX.delete"
    }

}
