package com.github.bkhablenko.bizzabo.feign.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TvmazeShow(

    @JsonProperty("id")
    val id: Int,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("image")
    val image: TvmazeImage,

    @JsonProperty("_embedded")
    val embedded: Embedded,
) {

    data class Embedded(

        @JsonProperty("cast")
        val cast: List<TvmazeCastMember>,
    )
}
