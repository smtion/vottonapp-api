package com.vottonapp.vottonapi.domain

import com.vottonapp.vottonapi.enumtype.VoteStatus

data class Vote(
    val answers: List<Answer>,
    var status: VoteStatus = VoteStatus.START,
)