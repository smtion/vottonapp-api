package com.vottonapp.vottonapi.config.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Configuration
@Order(-2)
class GlobalWebExceptionHandler(
    private val objectMapper: ObjectMapper
) : WebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {

        when (ex) {
            is AppException -> when (ex.code) {
                AppExceptionCode.NOT_FOUND -> exchange.response.statusCode = HttpStatus.NOT_FOUND
            }
            else -> exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        }

        exchange.response.headers.contentType = MediaType.APPLICATION_PROBLEM_JSON
        return exchange.response.writeWith(Mono.empty())
//        val bytes = objectMapper.writeValueAsBytes(responseDto)
//        val buffer = exchange.response.bufferFactory().wrap(bytes)
//        return exchange.response.writeWith(Mono.just(buffer))
    }
}