package com.ajaxproject.warehouse.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME )
annotation class RequestLimit   // TODO make limit settable, smth like: (val value: Int = 10)
