package com.vottonapp.vottonapi.config.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class AppException(status: HttpStatus, val code: AppExceptionCode) : ResponseStatusException(status) {

    constructor(code: AppExceptionCode): this(HttpStatus.BAD_REQUEST, code)
}