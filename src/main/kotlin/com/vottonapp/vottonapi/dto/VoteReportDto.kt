package com.vottonapp.vottonapi.dto

import com.vottonapp.vottonapi.domain.Answer
import com.vottonapp.vottonapi.domain.Voter

data class VoteReportDto(
    val answers: List<AnswerResult>
) {
    data class AnswerResult(
        val answer: Answer,
        val voters: MutableSet<Voter> = mutableSetOf(),
    ) {
        val count = voters.size
    }
}
