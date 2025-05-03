package ru.practicum.android.diploma.presentation.filters

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.domain.api.FiltersPrefsInteractor
import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.domain.models.main.Location

class FiltersViewModel(private val filtersPrefsInteractor: FiltersPrefsInteractor) : ViewModel() {

    private val _filterState = MutableStateFlow(filtersPrefsInteractor.getFilters().toUiState())
    val filterState: StateFlow<UiFiltersState> = _filterState

    private val _selectedIndustry = MutableStateFlow(_filterState.value.industry)
    val selectedIndustry: StateFlow<Industry?> = _selectedIndustry

    fun setIndustry(newIndustry: Industry) {
        _selectedIndustry.value = newIndustry
        _filterState.value = _filterState.value.copy(industry = newIndustry)
    }

    fun setSalary(newSalary: Int) {
        _filterState.value = _filterState.value.copy(salaryExpectations = newSalary)
    }

    fun setHideWithoutSalary(hide: Boolean) {
        _filterState.value = _filterState.value.copy(hideWithoutSalary = hide)
    }

    fun hasChanges(): Boolean {
        val currentFilters = filtersPrefsInteractor.getFilters().toUiState()
        return _filterState.value != currentFilters
    }

    fun saveAll() {
        val state = _filterState.value
        filtersPrefsInteractor.saveFilters(
            industryId = state.industry?.id,
            industryName = state.industry?.name,
            salary = state.salaryExpectations,
            hide = state.hideWithoutSalary,
            location = state.location
        )
    }

    fun clearSelectedIndustry() {
        _selectedIndustry.value = null
        _filterState.value = _filterState.value.copy(industry = null)
    }

    fun clearSalary() {
        _filterState.value = _filterState.value.copy(salaryExpectations = null)
    }

    fun clearAll() {
        _filterState.value = UiFiltersState()
        filtersPrefsInteractor.clearAll()
    }

    fun clearSelectedLocation() {
        _filterState.value = _filterState.value.copy(location = null)
    }

    fun discardChanges() {
        val saved = filtersPrefsInteractor.getFilters().toUiState()
        _filterState.value = saved
        _selectedIndustry.value = saved.industry
    }

    fun setCountry(countryId: Int, countryName: String) {
        val location = Location(
            countryId = countryId,
            countryName = countryName,
            regionId = 0,
            regionName = ""
        )
        _filterState.value = _filterState.value.copy(location = location)
    }

    fun setRegion(regionId: Int, regionName: String) {
        val currentLocation = _filterState.value.location ?: Location()
        val updatedLocation = currentLocation.copy(
            regionId = regionId,
            regionName = regionName
        )
        _filterState.value = _filterState.value.copy(location = updatedLocation)
    }
}
