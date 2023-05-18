package com.github.bkhablenko.bizzabo.web.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.bkhablenko.bizzabo.service.model.Show

data class GetScheduleResponse(

    @JsonProperty("shows")
    val shows: List<Show>,
)
