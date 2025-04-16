package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.main.VacancyShort

interface InteractorFavoriteVacancies {
    suspend fun insertVacancy(vacancy: VacancyShort): Result<Unit>

    suspend fun getAllVacancies(): List<VacancyShort>?

    suspend fun getById(vacancyId: Int): Result<VacancyShort>

    suspend fun deleteById(vacancyId: Int): Result<Unit>

    fun getVacanciesFlow(): Flow<List<VacancyShort>>

    suspend fun clearDatabase(): Result<Unit>
}
