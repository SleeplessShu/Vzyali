package ru.practicum.android.diploma.domain.repositories

import ru.practicum.android.diploma.domain.models.FiltersState

interface FiltersPrefsRepository {
    fun saveFilters(industryId: String?, industryName: String?, salary: Int?, hide: Boolean)
    fun getFilters(): FiltersState
    fun clearAll()
}
