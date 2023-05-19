package com.github.bkhablenko.bizzabo.domain.repository

import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisode
import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisodeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchedEpisodeRepository : JpaRepository<WatchedEpisode, WatchedEpisodeId> {

    fun findByUsername(username: String): List<WatchedEpisode>
}
