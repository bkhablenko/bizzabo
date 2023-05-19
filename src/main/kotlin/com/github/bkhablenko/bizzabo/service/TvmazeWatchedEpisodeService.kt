package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisode
import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisodeId
import com.github.bkhablenko.bizzabo.domain.repository.WatchedEpisodeRepository
import com.github.bkhablenko.bizzabo.service.integration.TvmazeIntegration
import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TvmazeWatchedEpisodeService(
    private val watchedEpisodeRepository: WatchedEpisodeRepository,
    private val tvmazeIntegration: TvmazeIntegration,
) : WatchedEpisodeService {

    override fun associateWithFirstUnwatchedEpisode(username: String, schedule: List<Show>): Map<Show, ShowEpisode> {
        val watchedEpisodes = getWatchedEpisodes(username)

        return schedule.associateWith { show ->
            tvmazeIntegration
                .getEpisodesByShowId(show.id)
                .first { episode -> episode.id !in watchedEpisodes }
        }
    }

    override fun saveWatchedEpisode(username: String, episodeId: Int) {
        val entity = WatchedEpisode(username, episodeId)
        watchedEpisodeRepository.save(entity)
    }

    override fun deleteWatchedEpisode(username: String, episodeId: Int) {
        val entityId = WatchedEpisodeId(username, episodeId)
        watchedEpisodeRepository.deleteById(entityId)
    }

    private fun getWatchedEpisodes(username: String): Set<Int> =
        watchedEpisodeRepository
            .findByUsername(username)
            .map { it.episodeId }
            .toSet()
}
