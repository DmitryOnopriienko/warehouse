package com.ajaxproject.warehouse.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/index")
    fun index() = "Hello, World!"
}
