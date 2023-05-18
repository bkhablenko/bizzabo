package com.github.bkhablenko.bizzabo.feign.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL

data class TvmazeImage(

    @JsonProperty("original")
    val original: URL,
)
