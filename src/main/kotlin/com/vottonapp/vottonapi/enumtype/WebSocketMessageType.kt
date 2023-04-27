package com.vottonapp.vottonapi.enumtype

enum class WebSocketMessageType {
    HEALTHCHECK,
    START,
    FINISH,
    VOTE,
    JOIN,
    LEAVE,
    CLOSE,
    ;
}