package com.github.bkhablenko.bizzabo.service.integration

import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntry
import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntryId
import com.github.bkhablenko.bizzabo.domain.repository.ScheduleEntryRepository
import com.github.bkhablenko.bizzabo.service.TvmazeScheduleService
import com.github.bkhablenko.bizzabo.service.model.Show
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.net.URL

@DisplayName("TvmazeScheduleService")
class TvmazeScheduleServiceTest {

    companion object {
        private const val GAME_OF_THRONES_IMDB = "tt0944947"
        private const val USERNAME = "John.Smith"
    }

    private val scheduleEntryRepository = mock<ScheduleEntryRepository>()

    private val tvmazeIntegration = mock<TvmazeIntegration>()

    private val tvmazeScheduleService = TvmazeScheduleService(scheduleEntryRepository, tvmazeIntegration)

    @DisplayName("getSchedule")
    @Nested
    inner class GetScheduleTest {

        @Test
        fun `should return expected TV shows`() {
            val scheduleEntries = listOf(ScheduleEntry(USERNAME, GAME_OF_THRONES_IMDB))
            whenever(scheduleEntryRepository.findByUsername(USERNAME)) doReturn scheduleEntries

            val expectedShow = Show(
                id = 82,
                title = "Game of Thrones",
                imageUrl = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
                cast = emptyList(),
            )
            whenever(tvmazeIntegration.getShowByImdb(GAME_OF_THRONES_IMDB)) doReturn expectedShow

            val result = tvmazeScheduleService.getSchedule(USERNAME)
            assertThat(result, contains(expectedShow))
        }
    }

    @DisplayName("saveScheduleEntry")
    @Nested
    inner class SaveScheduleEntryTest {

        @Test
        fun `should save expected entity`() {
            tvmazeScheduleService.saveScheduleEntry(USERNAME, GAME_OF_THRONES_IMDB)

            verify(scheduleEntryRepository).save(argThat {
                username == USERNAME && imdb == GAME_OF_THRONES_IMDB
            })
        }
    }

    @DisplayName("deleteScheduleEntry")
    @Nested
    inner class DeleteScheduleEntryTest {

        @Test
        fun `should delete expected entity by ID`() {
            tvmazeScheduleService.deleteScheduleEntry(USERNAME, GAME_OF_THRONES_IMDB)

            val expectedEntityId = ScheduleEntryId(USERNAME, GAME_OF_THRONES_IMDB)
            verify(scheduleEntryRepository).deleteById(expectedEntityId)
        }
    }
}
