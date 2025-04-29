package ru.practicum.android.diploma.presentation.filters.location.areachoice

sealed class FiltersUiError {
    object NoConnection : FiltersUiError()
    object BadRequest : FiltersUiError()
    object ServerError : FiltersUiError()
    object NoChildRegions : FiltersUiError()
}
