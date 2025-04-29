package ru.practicum.android.diploma.presentation.filters

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.domain.api.FiltersPrefsInteractor
import ru.practicum.android.diploma.domain.models.main.Industry

class FiltersViewModel(private val filtersPrefsInteractor: FiltersPrefsInteractor) : ViewModel() {
    private val _selectedIndustry = MutableStateFlow<Industry?>(null)
    val selectedIndustry: StateFlow<Industry?> = _selectedIndustry

    private val _filterState = MutableStateFlow(UiFiltersState())
    val filterState: StateFlow<UiFiltersState> = _filterState

    init {
        _filterState.value = getFilters()
        _selectedIndustry.value = _filterState.value.industry
    }

    private fun getFilters(): UiFiltersState {
        return filtersPrefsInteractor.getFilters().toUiState()
    }

    fun setIndustry(newIndustry: Industry) {
        _selectedIndustry.value = newIndustry
        _filterState.value = _filterState.value.copy(industry = newIndustry)
        Log.d("ChosenIndustry", "${newIndustry.id} - ${newIndustry.name}")
    }

    fun setSalary(newSalary: Int) {
        _filterState.value = _filterState.value.copy(salaryExpectations = newSalary)
    }

    fun setHideWithoutSalary(hide: Boolean) {
        _filterState.value = _filterState.value.copy(hideWithoutSalary = hide)
    }

    fun saveAll() {
        val state = _filterState.value
        filtersPrefsInteractor.saveFilters(
            industryId = state.industry?.id,
            industryName = state.industry?.name,
            salary = state.salaryExpectations,
            hide = state.hideWithoutSalary
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
}
