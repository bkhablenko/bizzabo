package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisode
import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisodeId
import com.github.bkhablenko.bizzabo.domain.repository.WatchedEpisodeRepository
import com.github.bkhablenko.bizzabo.service.integration.TvmazeIntegration
import com.github.bkhablenko.bizzabo.service.model.Show
import com.github.bkhablenko.bizzabo.service.model.ShowEpisode
import com.github.bkhablenko.bizzabo.service.model.WatchNextItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.iterableWithSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.net.URL
import java.time.LocalDate
import java.time.Month

@DisplayName("TvmazeWatchedEpisodeService")
class TvmazeWatchedEpisodeServiceTest {

    companion object {
        private const val GAME_OF_THRONES_SHOW_ID = 82

        private const val TEST_USERNAME = "John.Smith"
        private const val TEST_EPISODE_ID = 4952
    }

    private val watchedEpisodeRepository = mock<WatchedEpisodeRepository>()

    private val tvmazeIntegration = mock<TvmazeIntegration>()

    private val tvmazeWatchedEpisodeService = TvmazeWatchedEpisodeService(watchedEpisodeRepository, tvmazeIntegration)

    @DisplayName("getWatchNextList")
    @Nested
    inner class GetWatchNextListTest {

        @Test
        fun `should return the next unwatched episode for each TV show on the schedule`() {
            val show = Show(
                id = GAME_OF_THRONES_SHOW_ID,
                title = "Game of Thrones",
                imageUrl = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
                cast = emptyList(),
            )
            val episodes = listOf(
                ShowEpisode(
                    id = 4952,
                    title = "Winter is Coming",
                    season = 1,
                    number = 1,
                    airDate = LocalDate.of(2011, Month.APRIL, 17),
                ),
                ShowEpisode(
                    id = 4953,
                    title = "The Kingsroad",
                    season = 1,
                    number = 2,
                    airDate = LocalDate.of(2011, Month.APRIL, 24),
                ),
            )

            val watchedEpisodes = listOf(WatchedEpisode(TEST_USERNAME, episodes[0].id))
            whenever(watchedEpisodeRepository.findByUsername(TEST_USERNAME)) doReturn watchedEpisodes

            whenever(tvmazeIntegration.getEpisodesByShowId(GAME_OF_THRONES_SHOW_ID)) doReturn episodes

            val result = tvmazeWatchedEpisodeService.getWatchNextList(TEST_USERNAME, listOf(show))
            assertThat(result, iterableWithSize(1))
            assertThat(result, hasItem(WatchNextItem(show, episodes[1])))
        }
    }

    @DisplayName("saveWatchedEpisode")
    @Nested
    inner class SaveWatchedEpisodeTest {

        @Test
        fun `should save expected entity`() {
            tvmazeWatchedEpisodeService.saveWatchedEpisode(TEST_USERNAME, TEST_EPISODE_ID)

            verify(watchedEpisodeRepository).save(argThat {
                username == TEST_USERNAME && episodeId == TEST_EPISODE_ID
            })
        }
    }

    @DisplayName("deleteWatchedEpisode")
    @Nested
    inner class DeleteWatchedEpisodeTest {

        @Test
        fun `should delete expected entity by ID`() {
            tvmazeWatchedEpisodeService.deleteWatchedEpisode(TEST_USERNAME, TEST_EPISODE_ID)

            val expectedEntityId = WatchedEpisodeId(TEST_USERNAME, TEST_EPISODE_ID)
            verify(watchedEpisodeRepository).deleteById(expectedEntityId)
        }
    }
}
