package ru.practicum.android.diploma.domain.models

data class AreaFilter(
    val areas: List<AreaFilter>?,
    val id: String?,
    val name: String?,
    val parentId: String?
)
