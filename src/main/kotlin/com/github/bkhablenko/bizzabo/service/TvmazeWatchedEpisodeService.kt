package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisode
import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisodeId
import com.github.bkhablenko.bizzabo.domain.repository.WatchedEpisodeRepository
import com.github.bkhablenko.bizzabo.service.integration.TvmazeIntegration
import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.WatchNextItem
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TvmazeWatchedEpisodeService(
    private val watchedEpisodeRepository: WatchedEpisodeRepository,
    private val tvmazeIntegration: TvmazeIntegration,
) : WatchedEpisodeService {

    override fun getWatchNextList(username: String, schedule: List<Show>): List<WatchNextItem> {
        val watchedEpisodes = getWatchedEpisodeIdSet(username)

        return schedule.map { show ->
            val nextEpisode = tvmazeIntegration.getEpisodesByShowId(show.id).first { it.id !in watchedEpisodes }
            WatchNextItem(show, nextEpisode)
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

    private fun getWatchedEpisodeIdSet(username: String): Set<Int> =
        watchedEpisodeRepository
            .findByUsername(username)
            .map { it.episodeId }
            .toSet()
}
