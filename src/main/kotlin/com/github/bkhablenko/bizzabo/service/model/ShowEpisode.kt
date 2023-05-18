package com.github.bkhablenko.bizzabo.service.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class ShowEpisode(

    @JsonProperty("id")
    val id: Int,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("season")
    val season: Int,

    @JsonProperty("number")
    val number: Int,

    @JsonProperty("airDate")
    val airDate: LocalDate,
)
