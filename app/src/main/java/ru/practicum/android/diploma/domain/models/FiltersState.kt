package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.domain.models.main.Location

data class FiltersState(
    val industryName: String? = null,
    val industryId: String? = null,
    val salary: Int? = null,
    val hideWithoutSalary: Boolean = false,
    val location: Location? = null
) {
    val hasAny: Boolean
        get() = industryId != null ||
            salary != null || hideWithoutSalary || location != null
}
