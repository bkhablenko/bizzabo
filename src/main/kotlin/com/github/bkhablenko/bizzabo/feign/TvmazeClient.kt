package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "tvmaze", url = "https://api.tvmaze.com")
interface TvmazeClient {

    @GetMapping("/lookup/shows")
    fun getShowByImdb(@RequestParam imdb: String): TvmazeShow

    @GetMapping("/shows/{id}/cast")
    fun getShowCastById(@RequestParam id: Int): List<TvmazeCastMember> = emptyList()
}
