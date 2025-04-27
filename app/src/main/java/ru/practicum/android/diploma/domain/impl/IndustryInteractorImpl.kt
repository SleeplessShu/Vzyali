package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.domain.repositories.IndustryRepository

class IndustryInteractorImpl(
    private val repository: IndustryRepository
) : IndustryInteractor {
    override suspend fun getIndustries(): Resource<List<Industry>> {
        return repository.getIndustries()
    }
}
