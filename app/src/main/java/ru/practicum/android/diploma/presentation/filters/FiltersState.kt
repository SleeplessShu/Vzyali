package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.main.Industry

data class FiltersState(
    val industry: Industry? = null,
    val salaryRange: IntRange? = null,
) {
    val hasAny: Boolean
        get() = industry != null ||
            salaryRange != null
}
