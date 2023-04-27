package com.vottonapp.vottonapi.domain

import com.vottonapp.vottonapi.dto.VoteReportDto
import reactor.core.publisher.Mono

interface VoteInterface {

    fun createVote(code: String, answers: List<Answer>): Mono<Vote>

    fun getVote(code: String): Vote

    fun vote(code: String, voter: Voter, answerIndex: Int): Mono<Void>

    fun finishVote(code: String): Mono<Void>

    fun reportVote(code: String): Mono<VoteReportDto>

    fun close(code: String): Mono<Void>
}