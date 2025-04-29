package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FiltersState

interface FiltersPrefsInteractor {
    fun saveFilters(industryId: String?, industryName: String?, salary: Int?, hide: Boolean)
    fun getFilters(): FiltersState
    fun clearAll()
}
