package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntry
import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntryId
import com.github.bkhablenko.bizzabo.domain.repository.ScheduleEntryRepository
import com.github.bkhablenko.bizzabo.service.integration.TvmazeIntegration
import com.github.bkhablenko.bizzabo.service.model.Show
import org.springframework.stereotype.Service

@Service
class TvmazeScheduleService(
    private val scheduleEntryRepository: ScheduleEntryRepository,
    private val tvmazeIntegration: TvmazeIntegration,
) : ScheduleService {

    override fun getSchedule(username: String): List<Show> =
        scheduleEntryRepository
            .findByUsername(username)
            .map {
                tvmazeIntegration.getShowByImdb(it.imdb)
            }

    override fun saveScheduleEntry(username: String, imdb: String) {
        val entity = ScheduleEntry(username, imdb)
        scheduleEntryRepository.save(entity)
    }

    override fun deleteScheduleEntry(username: String, imdb: String) {
        val entityId = ScheduleEntryId(username, imdb)
        scheduleEntryRepository.deleteById(entityId)
    }
}
