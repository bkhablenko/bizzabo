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
        private const val GAME_OF_THRONES_SHOW_ID = 82
        private const val TEST_USERNAME = "John.Smith"
    }

    private val scheduleEntryRepository = mock<ScheduleEntryRepository>()

    private val tvmazeIntegration = mock<TvmazeIntegration>()

    private val tvmazeScheduleService = TvmazeScheduleService(scheduleEntryRepository, tvmazeIntegration)

    @DisplayName("getSchedule")
    @Nested
    inner class GetScheduleTest {

        @Test
        fun `should return expected TV shows`() {
            val scheduleEntries = listOf(ScheduleEntry(TEST_USERNAME, GAME_OF_THRONES_SHOW_ID))
            whenever(scheduleEntryRepository.findByUsername(TEST_USERNAME)) doReturn scheduleEntries

            val expectedShow = Show(
                id = GAME_OF_THRONES_SHOW_ID,
                title = "Game of Thrones",
                imageUrl = URL("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg"),
                cast = emptyList(),
            )
            whenever(tvmazeIntegration.getShowById(GAME_OF_THRONES_SHOW_ID)) doReturn expectedShow

            val result = tvmazeScheduleService.getSchedule(TEST_USERNAME)
            assertThat(result, contains(expectedShow))
        }
    }

    @DisplayName("saveScheduleEntry")
    @Nested
    inner class SaveScheduleEntryTest {

        @Test
        fun `should save expected entity`() {
            tvmazeScheduleService.saveScheduleEntry(TEST_USERNAME, GAME_OF_THRONES_SHOW_ID)

            verify(scheduleEntryRepository).save(argThat {
                username == TEST_USERNAME && showId == GAME_OF_THRONES_SHOW_ID
            })
        }
    }

    @DisplayName("deleteScheduleEntry")
    @Nested
    inner class DeleteScheduleEntryTest {

        @Test
        fun `should delete expected entity by ID`() {
            tvmazeScheduleService.deleteScheduleEntry(TEST_USERNAME, GAME_OF_THRONES_SHOW_ID)

            val expectedEntityId = ScheduleEntryId(TEST_USERNAME, GAME_OF_THRONES_SHOW_ID)
            verify(scheduleEntryRepository).deleteById(expectedEntityId)
        }
    }
}
