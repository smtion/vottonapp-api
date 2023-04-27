package com.vottonapp.vottonapi.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class IndexRouter {

    @Bean
    fun indexRoutes() = nest(path("/"),
        router {
            GET("") { ok().body(fromValue("Ok")) }
        }
    )
}