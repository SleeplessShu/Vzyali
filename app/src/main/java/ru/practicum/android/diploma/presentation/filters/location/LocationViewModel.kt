package ru.practicum.android.diploma.presentation.filters.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    private val _locationLiveData = MutableLiveData<LocationState>(LocationState.Empty)
    val locationLiveData: LiveData<LocationState> get() = _locationLiveData

    private var country: String = ""
    private var region: String = ""

    fun setCountry(selectedCountry: String) {
        country = selectedCountry
        updateState()
    }

    fun setRegion(selectedRegion: String) {
        region = selectedRegion
        updateState()
    }

    fun removeCountry() {
        country = ""
        updateState()
    }

    fun removeRegion() {
        region = ""
        updateState()
    }

    fun saveFilter() {

    }

    private fun updateState() {
        if (country.isEmpty() && region.isEmpty()) {
            _locationLiveData.postValue(LocationState.Empty)
        } else {
            _locationLiveData.postValue(LocationState.Content(country, region))
        }
    }
}
