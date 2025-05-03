package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FiltersPrefsInteractor
import ru.practicum.android.diploma.domain.models.FiltersState
import ru.practicum.android.diploma.domain.models.main.Location
import ru.practicum.android.diploma.domain.repositories.FiltersPrefsRepository

class FiltersPrefsInteractorImpl(
    private val filtersPrefsRepository: FiltersPrefsRepository
) : FiltersPrefsInteractor {
    override fun saveFilters(
        industryId: String?,
        industryName: String?,
        salary: Int?,
        hide: Boolean,
        location: Location?
    ) {
        filtersPrefsRepository.saveFilters(industryId, industryName, salary, hide, location)
    }

    override fun getFilters(): FiltersState {
        return filtersPrefsRepository.getFilters()
    }

    override fun clearAll() {
        filtersPrefsRepository.clearAll()
    }
}
