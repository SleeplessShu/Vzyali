package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.InteractorFavoriteVacancies
import ru.practicum.android.diploma.domain.models.ResponseDb
import ru.practicum.android.diploma.domain.models.main.VacancyLong
import ru.practicum.android.diploma.domain.repositories.RepositoryFavoriteVacancies

class InteractorFavoriteVacanciesImpl(
    private val repositoryVacancies: RepositoryFavoriteVacancies
) : InteractorFavoriteVacancies {
    override suspend fun favouritesContains(id: Int): Boolean {
        return repositoryVacancies.favouritesContains(id)
    }

    override suspend fun insertVacancy(vacancy: VacancyLong): Result<Unit> {
        return repositoryVacancies.insertVacancy(vacancy)
    }

    override suspend fun getAllVacancies(): List<VacancyLong>? {
        return repositoryVacancies.getAllVacancies()
    }

    override suspend fun getById(vacancyId: Int): Result<VacancyLong> {
        return repositoryVacancies.getById(vacancyId)
    }

    override suspend fun deleteById(vacancyId: Int): Result<Unit> {
        return repositoryVacancies.deleteById(vacancyId)
    }

    override fun getVacanciesFlow(): Flow<ResponseDb<List<VacancyLong>>> {
        return repositoryVacancies.getVacanciesFlow()
    }

    override suspend fun clearDatabase(): Result<Unit> {
        return repositoryVacancies.clearDatabase()
    }
}
