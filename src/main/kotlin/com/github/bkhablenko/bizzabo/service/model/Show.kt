package com.github.bkhablenko.bizzabo.service.model

import java.net.URL

data class Show(

    val id: Int,

    val title: String,

    val imageUrl: URL,

    val cast: List<CastMember>,
)
