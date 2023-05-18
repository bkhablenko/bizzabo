package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShowEpisode
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "tvmaze", url = "https://api.tvmaze.com")
interface TvmazeClient {

    @GetMapping("/shows/{showId}")
    fun getShowById(@PathVariable showId: Int): TvmazeShow

    @GetMapping("/shows/{showId}/cast")
    fun getCastByShowId(@PathVariable showId: Int): List<TvmazeCastMember>

    @GetMapping("/shows/{showId}/episodes")
    fun getEpisodesByShowId(@PathVariable showId: Int): List<TvmazeShowEpisode>
}
