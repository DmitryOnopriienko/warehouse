package com.ajaxproject.warehouse.exception

import java.io.Serial

class MethodRateLimitExceededException(message: String?) : RuntimeException(message) {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 106759828112291430L
    }
}
