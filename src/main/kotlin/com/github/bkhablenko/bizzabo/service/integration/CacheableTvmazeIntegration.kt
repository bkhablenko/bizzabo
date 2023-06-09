package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.exception.ShowNotFoundException
import com.github.bkhablenko.bizzabo.feign.TvmazeClient
import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.service.model.CastMember
import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode
import feign.FeignException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class CacheableTvmazeIntegration(private val tvmazeClient: TvmazeClient) : TvmazeIntegration {

    @Cacheable("getShowById")
    override fun getShowById(showId: Int): Show {
        val tvmazeShow = try {
            tvmazeClient.getShowById(showId)
        } catch (_: FeignException.NotFound) {
            throw ShowNotFoundException(showId)
        }
        return with(tvmazeShow) {
            Show(
                id = id,
                title = name,
                imageUrl = image.original,
                cast = embedded.cast.map { toCastMember(it.person) },
            )
        }
    }

    @Cacheable("getEpisodesByShowId")
    override fun getEpisodesByShowId(showId: Int): List<ShowEpisode> {
        val tvmazeShowEpisodes = try {
            tvmazeClient.getEpisodesByShowId(showId)
        } catch (_: FeignException.NotFound) {
            throw ShowNotFoundException(showId)
        }
        return tvmazeShowEpisodes.map {
            with(it) {
                ShowEpisode(
                    id = id,
                    title = name,
                    season = season,
                    number = number,
                    airDate = airdate,
                )
            }
        }
    }

    private fun toCastMember(source: TvmazeCastMember.Person) =
        with(source) {
            CastMember(
                id = id,
                fullName = name,
                imageUrl = image.original,
            )
        }
}
