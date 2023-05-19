package com.github.bkhablenko.bizzabo.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table

@Entity
@IdClass(WatchedEpisodeId::class)
@Table(name = "watched_episode")
class WatchedEpisode(

    @Id
    var username: String,

    @Id
    var episodeId: Int,
)
