package com.vottonapp.vottonapi.service

import com.vottonapp.vottonapi.component.ChannelManager
import com.vottonapp.vottonapi.component.VoteManager
import com.vottonapp.vottonapi.config.exception.AppException
import com.vottonapp.vottonapi.config.exception.AppExceptionCode
import com.vottonapp.vottonapi.domain.*
import com.vottonapp.vottonapi.dto.VoteReportDto
import com.vottonapp.vottonapi.dto.WebSocketMessageDto
import com.vottonapp.vottonapi.enumtype.VoteStatus
import com.vottonapp.vottonapi.enumtype.WebSocketMessageType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class VoteService(
    private val voteManager: VoteManager,
    private val channelManager: ChannelManager,
) : VoteInterface {

    fun existCode(code: String): Boolean =
        channelManager.hasSink(code)

    override fun getVote(code: String): Vote =
        voteManager.getVote(code) ?: throw AppException(AppExceptionCode.NOT_FOUND)

    override fun createVote(code: String, answers: List<Answer>): Mono<Vote> {
        val vote = Vote(answers)
        voteManager.init(code, vote)

        val wsDto = WebSocketMessageDto(WebSocketMessageType.START, channelManager.getCurrentCount(code))
        channelManager.getSink(code).tryEmitNext(wsDto)

        return vote.toMono()
    }

    override fun finishVote(code: String): Mono<Void> {
        voteManager.finish(code)

        val wsDto = WebSocketMessageDto(WebSocketMessageType.FINISH, channelManager.getCurrentCount(code))
        channelManager.getSink(code).tryEmitNext(wsDto)

        return Mono.empty()
    }

    override fun vote(code: String, voter: Voter, answerIndex: Int): Mono<Void> {
        voteManager.vote(code, voter, answerIndex)

        val wsDto = WebSocketMessageDto(WebSocketMessageType.VOTE, channelManager.getCurrentCount(code), voter.voterId)
        channelManager.getSink(code).tryEmitNext(wsDto)

        return Mono.empty()
    }

    override fun reportVote(code: String): Mono<VoteReportDto> {
        val voteResult = voteManager.getVoteResult(code)

        return if (voteResult == null) {
            Mono.error(Exception("Vote result is null"))
        } else {
            val answerResultList = voteResult.vote.answers.map { answer -> VoteReportDto.AnswerResult(answer) }
            voteResult.result.forEach { (voter, answerIndex) ->
                answerResultList[answerIndex].voters.add(voter)
            }

            VoteReportDto(answerResultList).toMono()
        }
    }

    override fun close(code: String): Mono<Void> {
        val wsDto = WebSocketMessageDto(WebSocketMessageType.CLOSE)
        channelManager.getSink(code).tryEmitNext(wsDto)
        channelManager.complete(code)

        return Mono.empty()
    }

    fun getAnswers(answers: List<String>): List<Answer> = answers.mapIndexed { index, value  -> Answer(index, value) }
}