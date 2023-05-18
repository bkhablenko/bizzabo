package com.github.bkhablenko.bizzabo.service

import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntry
import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntryId
import com.github.bkhablenko.bizzabo.domain.repository.ScheduleEntryRepository
import com.github.bkhablenko.bizzabo.service.integration.TvmazeIntegration
import com.github.bkhablenko.bizzabo.service.model.Show
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TvmazeScheduleService(
    private val scheduleEntryRepository: ScheduleEntryRepository,
    private val tvmazeIntegration: TvmazeIntegration,
) : ScheduleService {

    override fun getSchedule(username: String): List<Show> =
        scheduleEntryRepository
            .findByUsername(username)
            .map {
                tvmazeIntegration.getShowById(it.showId)
            }

    override fun saveScheduleEntry(username: String, showId: Int) {
        val entity = ScheduleEntry(username, showId)
        scheduleEntryRepository.save(entity)
    }

    override fun deleteScheduleEntry(username: String, showId: Int) {
        val entityId = ScheduleEntryId(username, showId)
        scheduleEntryRepository.deleteById(entityId)
    }
}
