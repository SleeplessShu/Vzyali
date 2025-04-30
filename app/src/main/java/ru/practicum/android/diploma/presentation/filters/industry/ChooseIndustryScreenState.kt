package ru.practicum.android.diploma.presentation.filters.industry

import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.presentation.search.UiError

sealed class ChooseIndustryScreenState {
    data object Loading : ChooseIndustryScreenState()

    /**
     * @param allData данные, загруженные с сервера
     * @param query поисковая строка
     * @param filteredData данные, отфильтрованные по [query]
     */
    data class Content(
        val allData: List<Industry>,
        val query: String = "",
        val filteredData: List<Industry> = emptyList()
    ) : ChooseIndustryScreenState() {

        val data: List<Industry>
            get() = if (query.isBlank()) {
                allData
            } else {
                filteredData
            }
    }

    data class Error(val type: UiError) : ChooseIndustryScreenState()
}
