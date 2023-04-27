package com.vottonapp.vottonapi.component

import com.vottonapp.vottonapi.domain.*
import com.vottonapp.vottonapi.enumtype.VoteStatus
import org.springframework.stereotype.Component

@Component
object VoteManager {

    private val voteMap = mutableMapOf<String, VoteResult>()

    fun init(code: String, vote: Vote) {
        voteMap[code] = VoteResult(vote)
    }

    fun vote(code: String, voter: Voter, answerIndex: Int) {
        if (voteMap.containsKey(code)) {
            voteMap[code]!!.result[voter] = answerIndex
        }
    }

    fun getVote(code: String): Vote? = voteMap[code]?.vote

    fun getVoteResult(code: String): VoteResult? = voteMap[code]

    fun finish(code: String) {
        getVote(code)?.status = VoteStatus.FINISH
    }
}