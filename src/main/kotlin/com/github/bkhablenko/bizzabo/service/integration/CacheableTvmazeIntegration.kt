package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.feign.TvmazeClient
import com.github.bkhablenko.bizzabo.service.model.CastMember
import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class CacheableTvmazeIntegration(private val tvmazeClient: TvmazeClient) : TvmazeIntegration {

    @Cacheable("tvmazeShowsById")
    override fun getShowById(showId: Int): Show =
        with(tvmazeClient.getShowById(showId)) {
            val cast = getCastByShowId(id)
            Show(id = id, title = name, imageUrl = image.original, cast = cast)
        }

    override fun getEpisodesByShowId(showId: Int): List<ShowEpisode> =
        tvmazeClient.getEpisodesByShowId(showId).map {
            with(it) {
                ShowEpisode(id = id, title = name, season = season, number = number, airDate = airdate)
            }
        }

    private fun getCastByShowId(showId: Int): List<CastMember> =
        tvmazeClient.getCastByShowId(showId).map {
            with(it.person) {
                CastMember(id = id, fullName = name, imageUrl = image.original)
            }
        }
}
