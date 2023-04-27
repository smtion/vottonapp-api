package com.vottonapp.vottonapi.domain

data class VoteResult(
    val vote: Vote,
    val result: MutableMap<Voter, Int> = mutableMapOf(),
)