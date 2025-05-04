package ru.practicum.android.diploma.domain.repositories

import ru.practicum.android.diploma.domain.models.FiltersState
import ru.practicum.android.diploma.domain.models.main.Location

interface FiltersPrefsRepository {
    fun saveFilters(
        industryId: String?,
        industryName: String?,
        salary: Int?,
        hide: Boolean,
        location: Location?
    )

    fun getFilters(): FiltersState
    fun clearAll()
}
