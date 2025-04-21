package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.utils.StringProvider
import ru.practicum.android.diploma.domain.interactor.SearchVacancyInteractor
import ru.practicum.android.diploma.domain.models.main.VacancyShort
import ru.practicum.android.diploma.presentation.search.SearchFragment.Companion.SEARCH_DELAY
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchVacancyInteractor: SearchVacancyInteractor,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _searchState = MutableStateFlow(SearchState<List<VacancyShort>>())
    val searchState: StateFlow<SearchState<List<VacancyShort>>> = _searchState


    private val searchDebouncer: (String) -> Unit = debounce(
        delayMillis = SEARCH_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { query ->
        searchVacancy(query)
    }


    fun searchVacancy(query: String, currentPage: Int = 0) {

        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(isLoading = true, query = query)

            searchVacancyInteractor.searchVacancy(query, currentPage).collect { (content, error, pages) ->
                if(!content.isNullOrEmpty())

                _searchState.value = when {
                    content != null -> SearchState(
                        isLoading = false,
                        content = content,
                        query = query,
                        pages = pages
                    )

                    error != null -> SearchState(
                        isLoading = false,
                        error = mapError(error),
                        query = query,
                        pages = pages
                    )

                    else -> SearchState(
                        isLoading = false,
                        error = UiError.ServerError,
                        query = query,
                        pages = pages
                    )
                }
            }
        }
    }

    fun searchTask(query: String) {
        searchDebouncer(query)
    }


    private fun mapError(message: String?): UiError {
        return when {
            message?.contains(stringProvider.getString(R.string.errors_No_connection)) == true -> UiError.NoConnection
            message?.contains(stringProvider.getString(R.string.errors_bad_request)) == true -> UiError.BadRequest
            message?.contains(stringProvider.getString(R.string.errors_not_found)) == true -> UiError.NotFound
            message?.contains(stringProvider.getString(R.string.errors_Server)) == true -> UiError.ServerError
            else -> UiError.ServerError
        }
    }
}
