package com.ajaxproject.warehouse.exception

class InternalEntityNotFoundException(message: String?) : RuntimeException(message) {
    constructor(errorList: List<String>) : this(null) {
        this.errorList = errorList
    }

    var errorList: List<String>? = null
}
