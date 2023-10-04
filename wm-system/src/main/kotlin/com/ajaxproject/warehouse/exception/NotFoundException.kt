package com.ajaxproject.warehouse.exception

import java.io.Serial

class NotFoundException(message: String?) : RuntimeException(message) {
    constructor(errorList: List<String>) : this(null) {
        this.errorList = errorList
    }

    var errorList: List<String>? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = 7934118737327701468L
    }
}
