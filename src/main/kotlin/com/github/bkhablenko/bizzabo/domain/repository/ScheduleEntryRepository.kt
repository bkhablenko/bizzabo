package com.github.bkhablenko.bizzabo.domain.repository

import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntry
import com.github.bkhablenko.bizzabo.domain.model.ScheduleEntryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduleEntryRepository : JpaRepository<ScheduleEntry, ScheduleEntryId> {

    fun findByUsername(username: String): List<ScheduleEntry>
}
