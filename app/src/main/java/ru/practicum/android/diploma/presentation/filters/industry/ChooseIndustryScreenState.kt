package ru.practicum.android.diploma.presentation.filters.industry

import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.presentation.search.UiError

sealed class ChooseIndustryScreenState {
    data object Loading : ChooseIndustryScreenState()

    data class Content(val data: List<Industry>) : ChooseIndustryScreenState()

    data class Error(val type: UiError) : ChooseIndustryScreenState()
}
