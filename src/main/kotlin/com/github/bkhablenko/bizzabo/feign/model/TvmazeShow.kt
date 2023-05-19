package com.github.bkhablenko.bizzabo.feign.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TvmazeShow(

    val id: Int,

    val name: String,

    val image: TvmazeImage,

    @JsonProperty("_embedded")
    val embedded: Embedded,
) {

    data class Embedded(

        val cast: List<TvmazeCastMember>,
    )
}
