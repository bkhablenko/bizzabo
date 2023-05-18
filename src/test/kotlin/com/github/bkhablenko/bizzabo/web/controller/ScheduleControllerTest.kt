package com.github.bkhablenko.bizzabo.web.controller

import com.github.bkhablenko.bizzabo.service.ScheduleService
import com.github.bkhablenko.bizzabo.service.model.CastMember
import com.github.bkhablenko.bizzabo.service.model.Show
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.net.URL

@WebMvcTest(ScheduleController::class)
class ScheduleControllerTest {

    companion object {
        private const val GAME_OF_THRONES_IMDB = "tt0944947"
        private const val USERNAME = "John.Smith"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var scheduleService: ScheduleService

    @DisplayName("GET /api/v1/user/schedule")
    @Nested
    inner class GetScheduleTest {

        @Test
        @WithMockUser(username = USERNAME)
        fun `should respond with 200 OK on success`() {
            val expectedShow = Show(
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
            whenever(scheduleService.getSchedule(USERNAME)) doReturn listOf(expectedShow)

            getSchedule().andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)

                    jsonPath("$.shows", hasSize<Any>(1))
                    jsonPath("$.shows[0].id", equalTo(82))
                    jsonPath("$.shows[0].title", equalTo("Game of Thrones"))
                    jsonPath(
                        "$.shows[0].imageUrl",
                        equalTo("https://static.tvmaze.com/uploads/images/original_untouched/190/476117.jpg")
                    )

                    jsonPath("$.shows[0].cast", hasSize<Any>(1))
                    jsonPath("$.shows[0].cast[0].id", equalTo(14075))
                    jsonPath("$.shows[0].cast[0].fullName", equalTo("Kit Harington"))
                    jsonPath(
                        "$.shows[0].cast[0].imageUrl",
                        equalTo("https://static.tvmaze.com/uploads/images/original_untouched/1/3229.jpg")
                    )
                }
            }
        }

        @Test
        fun `should respond with 401 Unauthorized on missing Authorization header`() {
            getSchedule().andExpect {
                status { isUnauthorized() }
                content {
                    bytes(ByteArray(0))
                }
            }
        }

        private fun getSchedule() = mockMvc.get("/api/v1/user/schedule").andDo { print() }
    }

    @DisplayName("POST /api/v1/user/schedule/shows")
    @Nested
    inner class SaveScheduleEntryTest {

        @Test
        @WithMockUser(username = USERNAME)
        fun `should respond with 204 No Content on success`() {
            saveScheduleEntry(GAME_OF_THRONES_IMDB).andExpect {
                status { isNoContent() }
                content {
                    bytes(ByteArray(0))
                }
            }
        }

        @Test
        @WithMockUser(username = USERNAME)
        fun `should respond with 400 Bad Request on invalid payload`() {
            saveScheduleEntry("42").andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `should respond with 401 Unauthorized on missing Authorization header`() {
            saveScheduleEntry(GAME_OF_THRONES_IMDB).andExpect {
                status { isUnauthorized() }
                content {
                    bytes(ByteArray(0))
                }
            }
        }

        private fun saveScheduleEntry(imdb: String) =
            mockMvc
                .post("/api/v1/user/schedule/shows") {
                    contentType = MediaType.APPLICATION_JSON
                    content = """{"imdb":"$imdb"}"""
                }
                .andDo { print() }
    }

    @DisplayName("DELETE /api/v1/user/schedule/shows/{imdb}")
    @Nested
    inner class DeleteScheduleEntryTest {

        @Test
        @WithMockUser(username = USERNAME)
        fun `should respond with 204 No Content on success`() {
            deleteScheduleEntry(GAME_OF_THRONES_IMDB).andExpect {
                status { isNoContent() }
                content {
                    bytes(ByteArray(0))
                }
            }
        }

        @Test
        @WithMockUser(username = USERNAME)
        fun `should respond with 400 Bad Request on invalid path variable`() {
            deleteScheduleEntry("42").andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `should respond with 401 Unauthorized on missing Authorization header`() {
            deleteScheduleEntry(GAME_OF_THRONES_IMDB).andExpect {
                status { isUnauthorized() }
                content {
                    bytes(ByteArray(0))
                }
            }
        }

        private fun deleteScheduleEntry(imdb: String) =
            mockMvc
                .delete("/api/v1/user/schedule/shows/$imdb")
                .andDo { print() }
    }
}
