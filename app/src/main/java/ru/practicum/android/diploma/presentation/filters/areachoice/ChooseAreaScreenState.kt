package ru.practicum.android.diploma.presentation.filters.areachoice

import ru.practicum.android.diploma.domain.models.AreaFilter

sealed class ChooseAreaScreenState {
    data object Loading : ChooseAreaScreenState()

    data class Content(val data: List<AreaFilter>) : ChooseAreaScreenState()

    data class Error(val type: FiltersUiError) : ChooseAreaScreenState()
}
