package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.domain.models.main.Location

data class UiFiltersState(
    val location: Location? = null,
    val industry: Industry? = null,
    val salaryExpectations: Int? = null,
    val hideWithoutSalary: Boolean = false,
) {
    val hasAny: Boolean
        get() = industry != null ||
            salaryExpectations != null || hideWithoutSalary || location != null
}
