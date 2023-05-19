package com.github.bkhablenko.bizzabo.domain.model

import java.io.Serializable

data class WatchedEpisodeId(val username: String = "", val episodeId: Int = 0) : Serializable
