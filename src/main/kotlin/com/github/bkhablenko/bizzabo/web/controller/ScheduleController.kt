package com.github.bkhablenko.bizzabo.web.controller

import com.github.bkhablenko.bizzabo.service.ScheduleService
import com.github.bkhablenko.bizzabo.validation.ImdbPattern
import com.github.bkhablenko.bizzabo.web.model.GetScheduleResponse
import com.github.bkhablenko.bizzabo.web.model.SaveScheduleEntryRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/schedule")
@PreAuthorize("isAuthenticated()")
@Validated
class ScheduleController(private val scheduleService: ScheduleService) {

    @GetMapping
    fun getSchedule(@AuthenticationPrincipal user: User): GetScheduleResponse {
        return GetScheduleResponse(shows = scheduleService.getSchedule(user.username))
    }

    @PostMapping("/shows")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun saveScheduleEntry(@AuthenticationPrincipal user: User, @RequestBody @Valid payload: SaveScheduleEntryRequest) {
        scheduleService.saveScheduleEntry(user.username, payload.imdb)
    }

    @DeleteMapping("/shows/{imdb}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    fun deleteScheduleEntry(@AuthenticationPrincipal user: User, @PathVariable @ImdbPattern imdb: String) {
        scheduleService.deleteScheduleEntry(user.username, imdb)
    }
}
