package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.WatchNextItem

interface WatchedEpisodeService {

    /**
     * Get the next unwatched episode for each show on the TV schedule.
     */
    fun getWatchNextList(username: String, schedule: List<Show>): List<WatchNextItem>

    fun saveWatchedEpisode(username: String, episodeId: Int)

    fun deleteWatchedEpisode(username: String, episodeId: Int)
}
