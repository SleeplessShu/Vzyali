package ru.practicum.android.diploma.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.main.VacancyLong
import ru.practicum.android.diploma.domain.models.main.VacancyShort

interface SearchVacancyInteractor {
    fun searchVacancy(vacancyName: String, currentPage: Int): Flow<Triple<List<VacancyShort>?, String?, Int>>
    fun searchVacancyDetails(vacancyId: String): Flow<Pair<VacancyLong?, String?>>
}
