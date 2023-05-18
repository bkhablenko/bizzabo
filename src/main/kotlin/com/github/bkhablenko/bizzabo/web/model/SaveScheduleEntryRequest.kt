package com.github.bkhablenko.bizzabo.web.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.validation.annotation.Validated

@Validated
data class SaveScheduleEntryRequest(

    @JsonProperty("showId")
    val showId: Int,
)
