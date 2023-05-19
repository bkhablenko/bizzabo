package com.github.bkhablenko.bizzabo.service.model

import java.time.LocalDate

data class ShowEpisode(

    val id: Int,

    val title: String,

    val season: Int,

    val number: Int,

    val airDate: LocalDate,
)
