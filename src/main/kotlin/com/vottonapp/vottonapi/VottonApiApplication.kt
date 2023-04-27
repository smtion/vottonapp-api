package com.vottonapp.vottonapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VottonApiApplication

fun main(args: Array<String>) {
	runApplication<VottonApiApplication>(*args)
}
