package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.domain.models.main.Location

data class FiltersState(
    val location: Location? = null,
    val industry: Industry? = null,
    val salaryRange: IntRange? = null,
) {
    val hasAny: Boolean
        get() = industry != null ||
            salaryRange != null || location != null
}
