package com.github.bkhablenko.bizzabo.service.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL

data class CastMember(

    @JsonProperty("id")
    val id: Int,

    @JsonProperty("fullName")
    val fullName: String,

    @JsonProperty("imageUrl")
    val imageUrl: URL,
)
