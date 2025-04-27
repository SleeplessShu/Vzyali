package ru.practicum.android.diploma.presentation.filters.areachoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.models.Resource

class AreaChoiceViewModel(private val areasInteractor: AreasInteractor) : ViewModel() {

    fun getAreas() {
        viewModelScope.launch {
            when (areasInteractor.getAreas()) {
                is Resource.Empty -> {}
                is Resource.Error -> {}
                is Resource.Success -> {}
            }
        }
    }
}
