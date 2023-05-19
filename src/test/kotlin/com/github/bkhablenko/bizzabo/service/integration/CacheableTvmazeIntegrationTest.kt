package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.config.CacheConfig
import com.github.bkhablenko.bizzabo.exception.ShowNotFoundException
import com.github.bkhablenko.bizzabo.feign.TvmazeClient
import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.feign.model.TvmazeImage
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShowEpisode
import com.github.bkhablenko.bizzabo.service.model.CastMember
import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode
import feign.FeignException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
import java.net.URL
import java.time.LocalDate
import java.time.Month

@DisplayName("CacheableTvmazeIntegration")
@SpringBootTest(classes = [CacheableTvmazeIntegration::class, CacheConfig::class])
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class CacheableTvmazeIntegrationTest {

    companion object {
        private const val GAME_OF_THRONES_SHOW_ID = 82
    }

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var tvmazeIntegration: TvmazeIntegration

    @MockBean
    private lateinit var tvmazeClient: TvmazeClient

    @BeforeEach
    fun setUp() {
        whenever(tvmazeClient.getShowById(GAME_OF_THRONES_SHOW_ID)) doReturn TvmazeShow(
            id = GAME_OF_THRONES_SHOW_ID,
            name = "Game of Thrones",
            image = TvmazeImage(
                original = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
            ),
        )
        whenever(tvmazeClient.getCastByShowId(GAME_OF_THRONES_SHOW_ID)) doReturn listOf(
            TvmazeCastMember(
                person = TvmazeCastMember.Person(
                    id = 14075,
                    name = "Kit Harington",
                    image = TvmazeImage(
                        original = URL("https://static.tvmaze.com/uploads/images/original_untouched/1/3229.jpg"),
                    ),
                ),
            ),
        )
        whenever(tvmazeClient.getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)) doReturn listOf(
            TvmazeShowEpisode(
                id = 4952,
                name = "Winter is Coming",
                season = 1,
                number = 1,
                airdate = LocalDate.of(2011, Month.APRIL, 17)

            )
        )
    }

    @AfterEach
    fun tearDown() {
        invalidateCaches()
    }

    @DisplayName("getShowById")
    @Nested
    inner class GetShowByIdTest {

        @Test
        fun `should return TV shows with their cast`() {
            val expectedValue = Show(
                id = GAME_OF_THRONES_SHOW_ID,
                title = "Game of Thrones",
                imageUrl = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
                cast = listOf(
                    CastMember(
                        id = 14075,
                        fullName = "Kit Harington",
                        imageUrl = URL("https://static.tvmaze.com/uploads/images/original_untouched/1/3229.jpg"),
                    ),
                ),
            )
            val result = tvmazeIntegration.getShowById(GAME_OF_THRONES_SHOW_ID)
            assertThat(result, equalTo(expectedValue))
        }

        @Test
        fun `should throw exception if not found`() {
            doThrow(FeignException.NotFound::class)
                .whenever(tvmazeClient).getShowById(GAME_OF_THRONES_SHOW_ID)

            assertThrows<ShowNotFoundException> {
                tvmazeIntegration.getShowById(GAME_OF_THRONES_SHOW_ID)
            }
        }

        @Test
        fun `should cache returned values`() {
            repeat(5) {
                tvmazeIntegration.getShowById(GAME_OF_THRONES_SHOW_ID)
            }
            verify(tvmazeClient, times(1)).getShowById(GAME_OF_THRONES_SHOW_ID)
        }
    }

    @DisplayName("getEpisodesByShowId")
    @Nested
    inner class GetEpisodesByShowIdTest {

        @Test
        fun `should return TV show episodes`() {
            val expectedValue = ShowEpisode(
                id = 4952,
                title = "Winter is Coming",
                season = 1,
                number = 1,
                airDate = LocalDate.of(2011, Month.APRIL, 17),
            )
            val result = tvmazeIntegration.getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)
            assertThat(result, contains(expectedValue))
        }

        @Test
        fun `should throw exception if not found`() {
            doThrow(FeignException.NotFound::class)
                .whenever(tvmazeClient).getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)

            assertThrows<ShowNotFoundException> {
                tvmazeIntegration.getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)
            }
        }

        @Test
        fun `should cache returned values`() {
            repeat(5) {
                tvmazeIntegration.getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)
            }
            verify(tvmazeClient, times(1)).getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)
        }
    }

    private fun invalidateCaches() = with(cacheManager) {
        cacheNames.mapNotNull { getCache(it) }.forEach { it.invalidate() }
    }
}
