package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.service.model.Show

interface TvmazeIntegration {

    fun getShowByImdb(imdb: String): Show
}
