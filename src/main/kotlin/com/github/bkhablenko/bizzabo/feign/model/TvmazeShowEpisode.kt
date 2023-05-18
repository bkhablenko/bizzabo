package com.github.bkhablenko.bizzabo.feign.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class TvmazeShowEpisode(

    @JsonProperty("id")
    val id: Int,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("season")
    val season: Int,

    @JsonProperty("number")
    val number: Int,

    @JsonProperty("airdate")
    val airdate: LocalDate,
)
