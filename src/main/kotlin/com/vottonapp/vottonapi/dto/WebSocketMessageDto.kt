package com.vottonapp.vottonapi.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.vottonapp.vottonapi.enumtype.WebSocketMessageType

data class WebSocketMessageDto(
    val messageType: WebSocketMessageType = WebSocketMessageType.HEALTHCHECK,
    val currentMemberCount: Int = 0,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val voterId: String? = null,
)
