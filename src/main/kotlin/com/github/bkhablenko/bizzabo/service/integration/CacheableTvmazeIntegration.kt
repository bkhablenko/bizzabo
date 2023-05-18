package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.feign.TvmazeClient
import com.github.bkhablenko.bizzabo.service.model.CastMember
import com.github.bkhablenko.bizzabo.service.model.Show
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class CacheableTvmazeIntegration(private val tvmazeClient: TvmazeClient) : TvmazeIntegration {

    @Cacheable("tvmazeShowsByImdb")
    override fun getShowByImdb(imdb: String): Show =
        with(tvmazeClient.getShowByImdb(imdb)) {
            val cast = getShowCastById(id)
            Show(id = id, title = name, imageUrl = image.original, cast = cast)
        }

    private fun getShowCastById(tvmazeShowId: Int): List<CastMember> =
        tvmazeClient.getShowCastById(tvmazeShowId).map {
            with(it.person) {
                CastMember(id = id, fullName = name, imageUrl = image.original)
            }
        }
}
