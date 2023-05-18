package com.github.bkhablenko.bizzabo.feign.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TvmazeCastMember(

    @JsonProperty("person")
    val person: Person,
) {

    data class Person(

        @JsonProperty("id")
        val id: Int,

        @JsonProperty("name")
        val name: String,

        @JsonProperty("image")
        val image: TvmazeImage,
    )
}
