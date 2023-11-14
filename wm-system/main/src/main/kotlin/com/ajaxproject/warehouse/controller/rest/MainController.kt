package com.ajaxproject.warehouse.controller.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.netty.http.server.HttpServerResponse

@RestController
class MainController {

    @GetMapping("/index")
    fun index(httpServletResponse: HttpServerResponse) = httpServletResponse.sendRedirect("/customers")
}
