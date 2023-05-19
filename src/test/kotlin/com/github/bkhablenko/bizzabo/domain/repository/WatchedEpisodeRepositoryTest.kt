package com.github.bkhablenko.bizzabo.domain.repository

import com.github.bkhablenko.bizzabo.domain.model.WatchedEpisode
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("WatchedEpisodeRepository")
class WatchedEpisodeRepositoryTest : AbstractDataJpaTest() {

    @Autowired
    private lateinit var watchedEpisodeRepository: WatchedEpisodeRepository

    @DisplayName("findByUsername")
    @Nested
    inner class FindByUsernameTest {

        @Test
        fun `should return expected TV shows`() {
            val username = "John.Smith"
            with(entityManager) {
                // Someone else's record
                persist(WatchedEpisode(username = "mculkin1980", episodeId = 10))

                persist(WatchedEpisode(username = username, episodeId = 1))
                persist(WatchedEpisode(username = username, episodeId = 2))
                persist(WatchedEpisode(username = username, episodeId = 3))

                flushAndClear()
            }

            val result = watchedEpisodeRepository.findByUsername(username)
            assertThat(result.map { it.episodeId }, containsInAnyOrder(1, 2, 3))
        }
    }
}
