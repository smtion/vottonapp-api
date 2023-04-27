package com.vottonapp.vottonapi.router

import com.vottonapp.vottonapi.handler.ApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class ApiRouter(
    private val apiHandler: ApiHandler
) {

    @Bean
    fun codeApiRoutes() = nest(path("/codes"),
        router {
            GET("{code}", apiHandler::existCode)
        }
    )

    @Bean
    fun voteApiRoutes() = nest(path("/votes"),
        router {
            GET("{code}", apiHandler::getVote)
            POST("{code}", apiHandler::createVote)
            POST("{code}/vote", apiHandler::vote)
            PUT("{code}", apiHandler::finishVote)
            GET("{code}/report", apiHandler::reportVote)
            DELETE("{code}", apiHandler::close)
        }
    )
}