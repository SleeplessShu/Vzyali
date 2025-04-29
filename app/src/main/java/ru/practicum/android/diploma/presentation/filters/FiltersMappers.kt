package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.FiltersState
import ru.practicum.android.diploma.domain.models.main.Industry

fun FiltersState.toUiState(): UiFiltersState {
    val industry = if (!industryId.isNullOrBlank() && !industryName.isNullOrBlank()) {
        Industry(id = industryId, name = industryName)
    } else {
        null
    }

    return UiFiltersState(
        industry = industry,
        salaryExpectations = salary,
        hideWithoutSalary = hideWithoutSalary
    )
}

fun UiFiltersState.toDomainState(): FiltersState {
    return FiltersState(
        industryId = industry?.id,
        industryName = industry?.name,
        salary = salaryExpectations,
        hideWithoutSalary = hideWithoutSalary
    )
}
