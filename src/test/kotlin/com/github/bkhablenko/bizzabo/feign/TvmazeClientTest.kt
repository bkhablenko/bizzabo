package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.feign.model.TvmazeImage
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShowEpisode
import feign.FeignException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

        @Test
        fun `should throw exception if not found`() {
            val nonExistentShowId = 0
            assertThrows<FeignException.NotFound> {
                tvmazeClient.getShowById(nonExistentShowId)
            }
        }
    }

    @DisplayName("getCastByShowId")
    @Nested
    inner class GetCastByShowIdTest {

        @Test
        fun `should return expected cast`() {
            val result = tvmazeClient.getCastByShowId(GAME_OF_THRONES_SHOW_ID)
            assertThat(result, hasSize(42))

            val expectedCastMember = TvmazeCastMember(
                person = TvmazeCastMember.Person(
                    id = 14075,
                    name = "Kit Harington",
                    image = TvmazeImage(
                        original = URL("https://static.tvmaze.com/uploads/images/original_untouched/1/3229.jpg"),
                    ),
                ),
            )
            assertThat(result, hasItem(expectedCastMember))
        }

        @Test
        fun `should throw exception if not found`() {
            val nonExistentShowId = 0
            assertThrows<FeignException.NotFound> {
                tvmazeClient.getCastByShowId(nonExistentShowId)
            }
        }
    }

    @DisplayName("getEpisodesByShowId")
    @Nested
    inner class GetEpisodesByShowIdTest {

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
            assertThat(result, hasItem(expectedFirstEpisode))
        }

        @Test
        fun `should throw exception if not found`() {
            val nonExistentShowId = 0
            assertThrows<FeignException.NotFound> {
                tvmazeClient.getEpisodesByShowId(nonExistentShowId)
            }
        }
    }
}
