package ru.practicum.android.diploma.presentation.filters.industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.utils.StringProvider
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.search.UiError

class ChooseIndustryViewModel(
    private val industryInteractor: IndustryInteractor,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow<ChooseIndustryScreenState>(ChooseIndustryScreenState.Loading)
    val state: StateFlow<ChooseIndustryScreenState> = _state

    init {
        viewModelScope.launch {
            when (val result = industryInteractor.getIndustries()) {
                is Resource.Success -> {
                    _state.value = ChooseIndustryScreenState.Content(result.data)
                }

                is Resource.Error -> {
                    _state.value = ChooseIndustryScreenState.Error(mapError(result.message))
                }

                is Resource.Empty -> Unit
            }
        }
    }

    fun search(query: String) {
        if (state.value is ChooseIndustryScreenState.Content) {
            val allIndustries = (state.value as ChooseIndustryScreenState.Content).allData
            _state.value = if (query.isBlank()) {
                ChooseIndustryScreenState.Content(allIndustries)
            } else {
                ChooseIndustryScreenState.Content(
                    allData = allIndustries,
                    filteredData = allIndustries.filter {
                        it.name.lowercase().contains(query.lowercase())
                    },
                    query = query
                )
            }
        }
    }

    private fun mapError(message: String): UiError {
        return when {
            message.contains(stringProvider.getString(R.string.errors_No_connection)) -> UiError.NoConnection
            else -> UiError.ServerError
        }
    }
}
