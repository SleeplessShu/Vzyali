package ru.practicum.android.diploma.presentation.filters.location

sealed interface LocationState {
    data object Empty : LocationState

    data class Content(
        val country: String,
        val region: String,
    ) : LocationState
}
