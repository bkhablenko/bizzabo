package com.github.bkhablenko.bizzabo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ShowNotFoundException(val showId: Int) : RuntimeException("Show not found: $showId")
