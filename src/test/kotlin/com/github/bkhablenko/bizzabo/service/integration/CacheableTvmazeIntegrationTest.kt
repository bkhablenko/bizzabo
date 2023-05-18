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
            val expectedShow = Show(
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
            assertThat(tvmazeIntegration.getShowById(GAME_OF_THRONES_SHOW_ID), equalTo(expectedShow))
        }

        @Test
        fun `should cache returned values`() {
            repeat(5) {
                tvmazeIntegration.getShowById(GAME_OF_THRONES_SHOW_ID)
            }
            verify(tvmazeClient, times(1)).getShowById(GAME_OF_THRONES_SHOW_ID)
        }
    }

    private fun invalidateCaches() = with(cacheManager) {
        cacheNames.mapNotNull { getCache(it) }.forEach { it.invalidate() }
    }
}
