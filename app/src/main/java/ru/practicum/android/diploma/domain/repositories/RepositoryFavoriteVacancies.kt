package ru.practicum.android.diploma.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ResponseDb
import ru.practicum.android.diploma.domain.models.main.VacancyLong

interface RepositoryFavoriteVacancies {
    suspend fun favouritesContains(id: Int): Boolean

    suspend fun insertVacancy(vacancy: VacancyLong): Result<Unit>

    suspend fun getAllVacancies(): List<VacancyLong>?

    suspend fun getById(vacancyId: Int): Result<VacancyLong>

    suspend fun deleteById(vacancyId: Int): Result<Unit>

    fun getVacanciesFlow(): Flow<ResponseDb<List<VacancyLong>>>

    suspend fun clearDatabase(): Result<Unit>
}
