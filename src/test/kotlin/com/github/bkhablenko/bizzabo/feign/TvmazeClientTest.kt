package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.feign.model.TvmazeImage
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShowEpisode
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URL
import java.time.LocalDate
import java.time.Month

@DisplayName("TvmazeClient")
class TvmazeClientTest : AbstractFeignClientTest() {

    companion object {
        private const val GAME_OF_THRONES_SHOW_ID = 82
    }

    @Autowired
    private lateinit var tvmazeClient: TvmazeClient

    @DisplayName("getShowById")
    @Nested
    inner class GetShowByIdTest {

        @Test
        fun `should return expected TV shows`() {
            val expectedShow = TvmazeShow(
                id = GAME_OF_THRONES_SHOW_ID,
                name = "Game of Thrones",
                image = TvmazeImage(
                    original = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
                ),
            )
            assertThat(tvmazeClient.getShowById(GAME_OF_THRONES_SHOW_ID), equalTo(expectedShow))
        }
    }

    @DisplayName("getShowEpisodesById")
    @Nested
    inner class GetShowEpisodesByIdTest {

        @Test
        fun `should return expected TV show episodes`() {
            val result = tvmazeClient.getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)
            assertThat(result, hasSize(73))

            val expectedFirstEpisode = TvmazeShowEpisode(
                id = 4952,
                name = "Winter is Coming",
                season = 1,
                number = 1,
                airdate = LocalDate.of(2011, Month.APRIL, 17)

            )
            assertThat(result.first(), equalTo(expectedFirstEpisode))
        }
    }
}
