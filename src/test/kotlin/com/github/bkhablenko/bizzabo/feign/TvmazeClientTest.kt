package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.feign.model.TvmazeImage
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
        fun `should return expected TV show`() {
            val result = tvmazeClient.getShowById(GAME_OF_THRONES_SHOW_ID)

            with(result) {
                assertThat(id, equalTo(GAME_OF_THRONES_SHOW_ID))
                assertThat(name, equalTo("Game of Thrones"))

                val expectedImage =
                    TvmazeImage(original = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"))
                assertThat(image, equalTo(expectedImage))
            }

            val kitHarington = TvmazeCastMember(
                person = TvmazeCastMember.Person(
                    id = 14075,
                    name = "Kit Harington",
                    image = TvmazeImage(
                        original = URL("https://static.tvmaze.com/uploads/images/original_untouched/1/3229.jpg"),
                    ),
                ),
            )

            with(result.embedded) {
                assertThat(cast, hasSize(42))
                assertThat(cast, hasItem(kitHarington))
            }
        }

        @Test
        fun `should throw exception if not found`() {
            val nonExistentShowId = 0
            assertThrows<FeignException.NotFound> {
                tvmazeClient.getShowById(nonExistentShowId)
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
                airdate = LocalDate.of(2011, Month.APRIL, 17),
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
