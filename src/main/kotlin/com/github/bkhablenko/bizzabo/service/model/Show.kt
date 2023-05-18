package com.github.bkhablenko.bizzabo.service.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL

data class Show(

    @JsonProperty("id")
    val id: Int,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("imageUrl")
    val imageUrl: URL,

    @JsonProperty("cast")
    val cast: List<CastMember>,
)
