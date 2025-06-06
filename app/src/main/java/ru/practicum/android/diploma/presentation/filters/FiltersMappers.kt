package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.domain.models.FiltersState
import ru.practicum.android.diploma.domain.models.main.Industry

fun FiltersState.toUiState(): UiFiltersState {
    val industry = if (!industryId.isNullOrBlank() && !industryName.isNullOrBlank()) {
        Industry(id = industryId, name = industryName)
    } else {
        null
    }

    return UiFiltersState(
        location = location,
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
        hideWithoutSalary = hideWithoutSalary,
        location = location
    )
}

fun FiltersState.toCountryLocation(): AreaFilter {
    return AreaFilter(
        areas = null,
        id = location?.countryId.toString(),
        name = location?.countryName,
        parentId = null
    )
}

fun FiltersState.toRegionLocation(): AreaFilter {
    return AreaFilter(
        areas = null,
        id = location?.regionId.toString(),
        name = location?.regionName,
        parentId = null
    )
}
