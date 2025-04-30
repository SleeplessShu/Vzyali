package ru.practicum.android.diploma.data.dto.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaFilterDto(
    val areas: List<AreaFilterDto>?,
    val id: String?,
    val name: String?,
    @SerialName("parent_id") val parentId: String?
)
