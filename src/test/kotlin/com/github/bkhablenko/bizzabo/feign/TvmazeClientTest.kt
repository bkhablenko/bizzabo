package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.feign.model.TvmazeImage
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URL

@DisplayName("TvmazeClient")
class TvmazeClientTest : AbstractFeignClientTest() {

    companion object {
        private const val GAME_OF_THRONES_IMDB = "tt0944947"
    }

    @Autowired
    private lateinit var tvmazeClient: TvmazeClient

    @DisplayName("getShowByImdb")
    @Nested
    inner class GetShowByImdbTest {

        @Test
        fun `should return expected TV shows`() {
            val expectedShow = TvmazeShow(
                id = 82,
                name = "Game of Thrones",
                image = TvmazeImage(
                    original = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
                ),
            )
            assertThat(tvmazeClient.getShowByImdb(GAME_OF_THRONES_IMDB), equalTo(expectedShow))
        }

        fun `should return null if nothing found`() {
            val nonExistentImdb = "tt0000000"
            assertThat(tvmazeClient.getShowByImdb(nonExistentImdb), nullValue())
        }
    }
}
