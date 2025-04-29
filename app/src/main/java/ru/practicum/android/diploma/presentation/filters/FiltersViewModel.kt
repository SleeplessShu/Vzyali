package ru.practicum.android.diploma.presentation.filters

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.domain.models.main.Location

class FiltersViewModel : ViewModel() {
    private val _selectedIndustry = MutableStateFlow<Industry?>(null)
    val selectedIndustry: StateFlow<Industry?> = _selectedIndustry

    private val _filterState = MutableStateFlow(FiltersState())
    val filterState: StateFlow<FiltersState> = _filterState

    fun setIndustry(newIndustry: Industry) {
        _selectedIndustry.value = newIndustry
        _filterState.value = _filterState.value.copy(industry = newIndustry)
    }

    fun clearSelectedIndustry() {
        _selectedIndustry.value = null
        _filterState.value = FiltersState()
    }

    fun clearSelectedLocation() {
        _filterState.value = _filterState.value.copy(location = null)
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
