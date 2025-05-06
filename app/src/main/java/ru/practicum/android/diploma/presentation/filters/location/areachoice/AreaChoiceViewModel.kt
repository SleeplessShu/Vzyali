package ru.practicum.android.diploma.presentation.filters.location.areachoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.utils.StringProvider
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.FiltersPrefsInteractor
import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.main.Location
import ru.practicum.android.diploma.presentation.filters.UiFiltersState
import ru.practicum.android.diploma.presentation.filters.toCountryLocation
import ru.practicum.android.diploma.presentation.filters.toRegionLocation

class AreaChoiceViewModel(
    private val filtersPrefsInteractor: FiltersPrefsInteractor,
    private val areasInteractor: AreasInteractor,
    private val stringProvider: StringProvider
) : ViewModel() {

    private var areasResult: List<AreaFilter>? = null

    private val _countryScreenState =
        MutableStateFlow<ChooseAreaScreenState>(ChooseAreaScreenState.Loading)
    val countryScreenState: StateFlow<ChooseAreaScreenState> = _countryScreenState

    private val _regionScreenState =
        MutableStateFlow<ChooseAreaScreenState>(ChooseAreaScreenState.Loading)
    val regionScreenState: StateFlow<ChooseAreaScreenState> = _regionScreenState

    private val _countryState = MutableStateFlow<AreaFilter?>(filtersPrefsInteractor.getFilters().toCountryLocation())
    val countryState: StateFlow<AreaFilter?> = _countryState

    private val _regionState = MutableStateFlow<AreaFilter?>(filtersPrefsInteractor.getFilters().toRegionLocation())
    val regionState: StateFlow<AreaFilter?> = _regionState

    fun getCountryAreas() {
        viewModelScope.launch {
            when (val result = areasInteractor.getAreas()) {
                is Resource.Empty -> Unit
                is Resource.Error -> {
                    _countryScreenState.value =
                        ChooseAreaScreenState.Error(mapError(result.message))
                }

                is Resource.Success -> {
                    _countryScreenState.value =
                        ChooseAreaScreenState.Content(getCountriesFromResult(result.data))
                }
            }
        }
    }

    fun getRegionAreas() {
        viewModelScope.launch {
            _regionScreenState.value = ChooseAreaScreenState.Loading
            when (val result = areasInteractor.getAreas()) {
                is Resource.Empty -> Unit
                is Resource.Error -> {
                    _regionScreenState.value =
                        ChooseAreaScreenState.Error(mapError(result.message))
                }

                is Resource.Success -> {
                    areasResult = result.data
                    if (_countryState.value != null) {
                        getRegionsByParent(areasResult!!, _countryState.value?.id!!)
                    } else {
                        getRegionsWithQuery()
                    }
                }
            }
        }
    }

    fun setCountryIfNull(area: AreaFilter) {
        viewModelScope.launch {
            if (_countryState.value == null) {
                setParentCountry(area)
            }
        }
    }

    private fun getCountriesFromResult(areas: List<AreaFilter>): List<AreaFilter> {
        val result = mutableListOf<AreaFilter>()

        for (area in areas) {
            if (area.parentId == null) {
                result.add(area)
            }
        }
        sortCountriesResult(result)
        return result
    }

    private fun sortCountriesResult(areas: MutableList<AreaFilter>) {
        val iterator = areas.iterator()
        var others: AreaFilter? = null

        while (iterator.hasNext()) {
            val area = iterator.next()
            if (area.name == "Другие регионы") {
                others = area
                iterator.remove()
            }
        }

        if (others == null) {
            return
        } else {
            areas.add(others)
        }
    }

    private fun setParentCountry(childElement: AreaFilter) {
        _countryState.value = findRootParent(childElement)
    }

    private fun findRootParent(element: AreaFilter): AreaFilter {
        if (element.parentId == null) return element

        val parent =
            areasResult?.flatMap { it.flatten() }?.firstOrNull { it.id == element.parentId }

        return parent?.let { findRootParent(it) } ?: element
    }

    private fun AreaFilter.flatten(): List<AreaFilter> {
        return listOf(this) + (areas?.flatMap { it.flatten() } ?: emptyList())
    }

    fun getRegionsWithQuery(
        query: String = ""
    ) {
        if (areasResult == null) return
        val areas = areasResult!!
        val result = mutableListOf<AreaFilter>()

        if (query.isNotEmpty()) {
            addRegionsWithQuery(areas, result, query)
        } else {
            addRegions(areas, result)
        }

        result.sortBy { it.name }

        if (result.isEmpty()) {
            _regionScreenState.value = ChooseAreaScreenState.Error(FiltersUiError.BadRequest)
        } else {
            _regionScreenState.value = ChooseAreaScreenState.Content(result)
        }
    }

    private fun getRegionsByParent(
        areas: List<AreaFilter>,
        parentId: String
    ) {
        val result = mutableListOf<AreaFilter>()

        addRegionsWithParent(areas, result, parentId)

        result.sortBy { it.name }

        areasResult = result

        if (result.isEmpty()) {
            _regionScreenState.value = ChooseAreaScreenState.Error(FiltersUiError.NoChildRegions)
        } else {
            _regionScreenState.value = ChooseAreaScreenState.Content(result)
        }
    }

    private fun addRegions(areas: List<AreaFilter>, result: MutableList<AreaFilter>) {
        for (area in areas) {
            if (area.parentId != null) result.add(area)
            if (!area.areas.isNullOrEmpty()) {
                addRegions(area.areas, result)
            }
        }
    }

    private fun addRegionsWithQuery(
        areas: List<AreaFilter>,
        result: MutableList<AreaFilter>,
        query: String
    ) {
        for (area in areas) {
            if (area.parentId != null && area.name != null && area.name.contains(
                    query,
                    ignoreCase = true
                )
            ) {
                result.add(area)
            }
            if (!area.areas.isNullOrEmpty()) {
                addRegionsWithQuery(area.areas, result, query)
            }
        }
    }

    private fun addRegionsWithParent(
        areas: List<AreaFilter>,
        result: MutableList<AreaFilter>,
        parentId: String
    ) {
        for (area in areas) {
            if (area.parentId == parentId) {
                result.add(area)
                if (!area.areas.isNullOrEmpty()) {
                    addRegionsWithParent(area.areas, result, area.id!!)
                }
            } else {
                if (!area.areas.isNullOrEmpty()) {
                    addRegionsWithParent(area.areas, result, parentId)
                }
            }
        }
    }

    private fun mapError(message: String): FiltersUiError {
        return when {
            message.contains(stringProvider.getString(R.string.errors_No_connection)) -> FiltersUiError.NoConnection
            else -> FiltersUiError.ServerError
        }
    }

    fun getLocation(): Location {
        return Location(
            countryId = _countryState.value?.id?.toIntOrNull() ?: -1,
            countryName = _countryState.value?.name.orEmpty(),
            regionId = _regionState.value?.id?.toIntOrNull() ?: -1,
            regionName = _regionState.value?.name.orEmpty()
        )
    }

    fun setCountry(country: AreaFilter) {
        _countryState.value = country
        _regionState.value = null
    }

    fun setRegion(region: AreaFilter) {
        _regionState.value = region
    }

    fun removeCountry() {
        _countryState.value = null
        _regionState.value = null
    }

    fun removeRegion() {
        _regionState.value = null
    }

    fun initLocationFromFiltersState(state: UiFiltersState) {
        val country = state.location?.let {
            if (!it.countryName.isNullOrBlank()) {
                AreaFilter(
                    id = it.countryId.toString(),
                    name = it.countryName,
                    parentId = null,
                    areas = null
                )
            } else null
        }

        val region = state.location?.let {
            if (!it.regionName.isNullOrBlank()) {
                AreaFilter(
                    id = it.regionId.toString(),
                    name = it.regionName,
                    parentId = it.countryId.toString(), // если надо связать
                    areas = null
                )
            } else null
        }

        _countryState.value = country
        _regionState.value = region
    }
}
