package com.github.bkhablenko.bizzabo.feign.model

data class TvmazeCastMember(

    val person: Person,
) {

    data class Person(

        val id: Int,

        val name: String,

        val image: TvmazeImage,
    )
}
