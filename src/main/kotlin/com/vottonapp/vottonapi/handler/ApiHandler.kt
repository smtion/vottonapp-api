package com.vottonapp.vottonapi.handler

import com.vottonapp.vottonapi.domain.Voter
import com.vottonapp.vottonapi.dto.VoteCreationDto
import com.vottonapp.vottonapi.dto.VoteAnswerDto
import com.vottonapp.vottonapi.service.VoteService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class ApiHandler(
    private val voteService: VoteService,
) {

    fun existCode(req: ServerRequest): Mono<ServerResponse> =
        ok().body(voteService.existCode(
            req.pathVariable("code")
        ).toMono())

    fun getVote(req: ServerRequest): Mono<ServerResponse> =
        ok().body(voteService.getVote(
            req.pathVariable("code")
        ).toMono())

    fun createVote(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<VoteCreationDto>()
            .flatMap { dto ->
                created(req.uri()).body(voteService.createVote(
                    req.pathVariable("code"),
                    voteService.getAnswers(dto.answers)
                ))
            }

    fun finishVote(req: ServerRequest): Mono<ServerResponse> =
        ok().body(voteService.finishVote(
            req.pathVariable("code")
        ))

    fun reportVote(req: ServerRequest): Mono<ServerResponse> =
        ok().body(voteService.reportVote(
            req.pathVariable("code")
        ))

    fun vote(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<VoteAnswerDto>()
            .flatMap { dto ->
                ok().body(voteService.vote(
                    req.pathVariable("code"),
                    Voter(dto.voterId),
                    dto.answerIndex
                ))
            }

    fun close(req: ServerRequest): Mono<ServerResponse> =
        ok().body(voteService.close(
            req.pathVariable("code")
        ))
}