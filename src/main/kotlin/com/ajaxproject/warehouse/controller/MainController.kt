package com.ajaxproject.warehouse.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/index")
    fun index(httpServletResponse: HttpServletResponse) = httpServletResponse.sendRedirect("/customers/mongo")
}
