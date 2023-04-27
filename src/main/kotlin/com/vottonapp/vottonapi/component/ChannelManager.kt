package com.vottonapp.vottonapi.component

import com.vottonapp.vottonapi.dto.WebSocketMessageDto
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
object ChannelManager {

    private val sinkMap = mutableMapOf<String, Sinks.Many<WebSocketMessageDto>>()

    private fun getOrCreate(key: String): Sinks.Many<WebSocketMessageDto> {
        if (!sinkMap.containsKey(key)) {
            sinkMap[key] = Sinks.many().multicast().onBackpressureBuffer(10, false)
            println(">> Create a sink for $key")
        }

        return sinkMap[key]!!
    }

    fun init(key: String) = this.getOrCreate(key)

    fun getSink(key: String) = this.getOrCreate(key)

    fun getFlux(key: String) = this.getOrCreate(key).asFlux()

    fun hasSink(key: String) = sinkMap.containsKey(key)

    fun getCurrentCount(key: String) = sinkMap[key]?.currentSubscriberCount() ?: 0

    fun complete(key: String) {
        sinkMap[key]?.tryEmitComplete()
        sinkMap.remove(key)
    }
}