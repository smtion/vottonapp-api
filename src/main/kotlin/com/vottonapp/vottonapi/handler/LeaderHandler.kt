package com.vottonapp.vottonapi.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.vottonapp.vottonapi.component.ChannelManager
import com.vottonapp.vottonapi.dto.WebSocketMessageDto
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class LeaderHandler(
    private val objectMapper: ObjectMapper,
    private val channelManager: ChannelManager,
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val code = session.attributes["code"] as String
        println("[$code] Created")
        channelManager.init(code)

        // sent from client
        session.receive()
            .map { deserialize(it.payloadAsText) }
            .subscribe(
                {  },
                { error: Throwable -> println("Error: $error") },
                { println("[$code] WebSocketSession is closed") }
            )

        // Send message to client
        return session.send(channelManager.getFlux(code).map { dto ->
            session.textMessage(serialize(dto))
        })
    }

    private fun deserialize(json: String): WebSocketMessageDto = objectMapper.readValue(json)

    private fun serialize(dto: WebSocketMessageDto): String = objectMapper.writeValueAsString(dto)
}