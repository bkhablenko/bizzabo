package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.service.model.Show

interface ScheduleService {

    fun getSchedule(username: String): List<Show>

    fun saveScheduleEntry(username: String, showId: Int)

    fun deleteScheduleEntry(username: String, showId: Int)
}
