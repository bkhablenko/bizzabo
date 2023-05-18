package com.github.bkhablenko.bizzabo.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table

@Entity
@IdClass(ScheduleEntryId::class)
@Table(name = "schedule")
class ScheduleEntry(

    @Id
    var username: String,

    @Id
    var imdb: String,
)
