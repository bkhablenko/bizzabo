package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode

interface TvmazeIntegration {

    fun getShowById(showId: Int): Show

    fun getEpisodesByShowId(showId: Int): List<ShowEpisode>
}
