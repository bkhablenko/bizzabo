package com.github.bkhablenko.bizzabo.web.controller

import com.github.bkhablenko.bizzabo.service.WatchedEpisodeService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@WebMvcTest(WatchedEpisodeController::class)
class WatchedEpisodeControllerTest {

    companion object {
        private const val TEST_USERNAME = "John.Smith"
        private const val TEST_EPISODE_ID = 4952
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var watchedEpisodeService: WatchedEpisodeService

    @DisplayName("POST /api/v1/user/watched-episodes")
    @Nested
    inner class SaveWatchedEpisodeTest {

        @Test
        @WithMockUser(username = TEST_USERNAME)
        fun `should respond with 204 No Content on success`() {
            saveWatchedEpisode(TEST_EPISODE_ID).andExpect {
                status { isNoContent() }
                content {
                    bytes(ByteArray(0))
                }
            }
            verify(watchedEpisodeService).saveWatchedEpisode(TEST_USERNAME, TEST_EPISODE_ID)
        }

        @Test
        fun `should respond with 401 Unauthorized on missing Authorization header`() {
            saveWatchedEpisode(TEST_EPISODE_ID).andExpect {
                status { isUnauthorized() }
                content {
                    bytes(ByteArray(0))
                }
            }
            verifyNoInteractions(watchedEpisodeService)
        }

        private fun saveWatchedEpisode(episodeId: Int) =
            mockMvc
                .post("/api/v1/user/watched-episodes") {
                    contentType = MediaType.APPLICATION_JSON
                    content = """{"episodeId":$episodeId}"""
                }
                .andDo { print() }
    }

    @DisplayName("DELETE /api/v1/user/watched-episodes/{episodeId}")
    @Nested
    inner class DeleteWatchedEpisodeTest {

        @Test
        @WithMockUser(username = TEST_USERNAME)
        fun `should respond with 204 No Content on success`() {
            deleteWatchedEpisode(TEST_EPISODE_ID).andExpect {
                status { isNoContent() }
                content {
                    bytes(ByteArray(0))
                }
            }
            verify(watchedEpisodeService).deleteWatchedEpisode(TEST_USERNAME, TEST_EPISODE_ID)
        }

        @Test
        fun `should respond with 401 Unauthorized on missing Authorization header`() {
            deleteWatchedEpisode(TEST_EPISODE_ID).andExpect {
                status { isUnauthorized() }
                content {
                    bytes(ByteArray(0))
                }
            }
            verifyNoInteractions(watchedEpisodeService)
        }

        private fun deleteWatchedEpisode(episodeId: Int) =
            mockMvc
                .delete("/api/v1/user/watched-episodes/$episodeId")
                .andDo { print() }
    }
}
