package com.github.bkhablenko.bizzabo.web.controller

import com.github.bkhablenko.bizzabo.service.WatchedEpisodeService
import com.github.bkhablenko.bizzabo.web.model.SaveWatchedEpisodeRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/watched-episodes")
@PreAuthorize("isAuthenticated()")
class WatchedEpisodeController(private val watchedEpisodeService: WatchedEpisodeService) {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun saveWatchedEpisode(@AuthenticationPrincipal user: User, @RequestBody payload: SaveWatchedEpisodeRequest) {
        watchedEpisodeService.saveWatchedEpisode(user.username, payload.episodeId)
    }

    @DeleteMapping("/{episodeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWatchedEpisode(@AuthenticationPrincipal user: User, @PathVariable episodeId: Int) {
        watchedEpisodeService.deleteWatchedEpisode(user.username, episodeId)
    }
}
