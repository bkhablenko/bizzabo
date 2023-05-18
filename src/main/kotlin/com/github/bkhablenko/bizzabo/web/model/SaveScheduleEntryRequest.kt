package com.github.bkhablenko.bizzabo.web.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.bkhablenko.bizzabo.validation.ImdbPattern
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

@Validated
data class SaveScheduleEntryRequest(

    @JsonProperty("imdb")
    @field:NotNull
    @field:ImdbPattern
    val imdb: String,
)
