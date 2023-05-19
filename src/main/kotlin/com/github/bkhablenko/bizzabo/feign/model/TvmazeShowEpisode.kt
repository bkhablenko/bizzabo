package com.github.bkhablenko.bizzabo.feign.model

import java.time.LocalDate

data class TvmazeShowEpisode(

    val id: Int,

    val name: String,

    val season: Int,

    val number: Int,

    val airdate: LocalDate,
)
