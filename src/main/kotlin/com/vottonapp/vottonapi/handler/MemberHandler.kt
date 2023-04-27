package com.vottonapp.vottonapi.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.vottonapp.vottonapi.component.ChannelManager
import com.vottonapp.vottonapi.component.ChannelManager.getCurrentCount
import com.vottonapp.vottonapi.component.VoteManager
import com.vottonapp.vottonapi.dto.WebSocketMessageDto
import com.vottonapp.vottonapi.enumtype.WebSocketMessageType
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.nio.channels.Channel

class MemberHandler(
    private val objectMapper: ObjectMapper,
    private val channelManager: ChannelManager,
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val code = session.attributes["code"] as String
        println("[$code] Joined member")

        val wsDto = WebSocketMessageDto(WebSocketMessageType.JOIN, channelManager.getCurrentCount(code))
        channelManager.getSink(code).tryEmitNext(wsDto)

        // sent from client
        session.receive()
            .map { deserialize(it.payloadAsText) }
            .subscribe(
                {  },
                { error: Throwable -> println("Error: $error") },
                {
                    println("[$code] WebSocketSession is closed")
                    if (channelManager.hasSink(code)) {
                        val wsDto = WebSocketMessageDto(WebSocketMessageType.LEAVE, channelManager.getCurrentCount(code) - 2)
                        channelManager.getSink(code).tryEmitNext(wsDto)
                    }
                }
            )

        // Send message to client
        return session.send(channelManager.getFlux(code).map { dto ->
            session.textMessage(serialize(dto))
        })
    }

    private fun deserialize(json: String): WebSocketMessageDto = objectMapper.readValue(json)

    private fun serialize(dto: WebSocketMessageDto): String = objectMapper.writeValueAsString(dto)
}
