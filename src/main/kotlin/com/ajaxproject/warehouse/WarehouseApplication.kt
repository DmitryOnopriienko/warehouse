package com.ajaxproject.warehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WarehouseApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<WarehouseApplication>(*args)
}
