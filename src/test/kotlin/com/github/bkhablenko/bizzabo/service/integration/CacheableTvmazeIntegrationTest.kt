package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.config.CacheConfig
import com.github.bkhablenko.bizzabo.feign.TvmazeClient
import com.github.bkhablenko.bizzabo.feign.model.TvmazeCastMember
import com.github.bkhablenko.bizzabo.feign.model.TvmazeImage
import com.github.bkhablenko.bizzabo.feign.model.TvmazeShow
import com.github.bkhablenko.bizzabo.service.model.CastMember
import com.github.bkhablenko.bizzabo.service.model.Show
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
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

@DisplayName("CacheableTvmazeIntegration")
@SpringBootTest(classes = [CacheableTvmazeIntegration::class, CacheConfig::class])
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class CacheableTvmazeIntegrationTest {

    companion object {
        private const val GAME_OF_THRONES_IMDB = "tt0944947"
    }

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var tvmazeIntegration: TvmazeIntegration

    @MockBean
    private lateinit var tvmazeClient: TvmazeClient

    @BeforeEach
    fun setUp() {
        val tvmazeShowId = 82

        whenever(tvmazeClient.getShowByImdb(GAME_OF_THRONES_IMDB)) doReturn TvmazeShow(
            id = tvmazeShowId,
            name = "Game of Thrones",
            image = TvmazeImage(
                original = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
            ),
        )
        whenever(tvmazeClient.getShowCastById(tvmazeShowId)) doReturn listOf(
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
    }

    @AfterEach
    fun tearDown() {
        invalidateCaches()
    }

    @DisplayName("getShowByImdb")
    @Nested
    inner class GetShowByImdbTest {

        @Test
        fun `should return TV shows with their cast`() {
            val expected = Show(
                id = 82,
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
            assertThat(tvmazeIntegration.getShowByImdb(GAME_OF_THRONES_IMDB), equalTo(expected))
        }

        @Test
        fun `should cache returned values`() {
            repeat(5) {
                tvmazeIntegration.getShowByImdb(GAME_OF_THRONES_IMDB)
            }
            verify(tvmazeClient, times(1)).getShowByImdb(GAME_OF_THRONES_IMDB)
        }
    }

    private fun invalidateCaches() = with(cacheManager) {
        cacheNames.mapNotNull { getCache(it) }.forEach { it.invalidate() }
    }
}
