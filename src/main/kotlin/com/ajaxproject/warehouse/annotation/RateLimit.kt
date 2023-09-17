package com.ajaxproject.warehouse.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RateLimit(val value: Int = 10)   // TODO make limit settable, smth like: (val value: Int = 10)
