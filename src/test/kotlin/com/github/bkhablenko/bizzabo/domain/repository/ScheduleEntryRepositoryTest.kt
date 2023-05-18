package com.github.bkhablenko.bizzabo.domain.repository

import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntry
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("ScheduleEntryRepository")
class ScheduleEntryRepositoryTest : AbstractDataJpaTest() {

    @Autowired
    private lateinit var scheduleEntryRepository: ScheduleEntryRepository

    @DisplayName("findByUsername")
    @Nested
    inner class FindByUsernameTest {

        @Test
        fun `should return expected TV shows`() {
            val username = "John.Smith"
            with(entityManager) {
                // Someone else's record
                persist(ScheduleEntry(username = "mculkin1980", showId = 10))

                persist(ScheduleEntry(username = username, showId = 1))
                persist(ScheduleEntry(username = username, showId = 2))
                persist(ScheduleEntry(username = username, showId = 3))

                flushAndClear()
            }

            val result = scheduleEntryRepository.findByUsername(username)
            assertThat(result.map { it.showId }, containsInAnyOrder(1, 2, 3))
        }
    }
}
