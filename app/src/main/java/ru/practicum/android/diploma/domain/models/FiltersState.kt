package ru.practicum.android.diploma.domain.models

data class FiltersState(
    val industryName: String? = null,
    val industryId: String? = null,
    val salary: Int? = null,
    val hideWithoutSalary: Boolean = false
)
