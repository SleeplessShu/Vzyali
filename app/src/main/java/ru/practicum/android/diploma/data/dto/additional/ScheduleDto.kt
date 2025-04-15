package ru.practicum.android.diploma.data.dto.additional

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto(
    val id: String,
    val name: String
)
