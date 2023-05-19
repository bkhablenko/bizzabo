package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode

interface WatchedEpisodeService {

    fun associateWithFirstUnwatchedEpisode(username: String, schedule: List<Show>): Map<Show, ShowEpisode>

    fun saveWatchedEpisode(username: String, episodeId: Int)

    fun deleteWatchedEpisode(username: String, episodeId: Int)
}
