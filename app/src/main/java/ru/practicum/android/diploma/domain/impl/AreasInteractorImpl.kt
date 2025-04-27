package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.repositories.AreasRepository

class AreasInteractorImpl(private val repository: AreasRepository) : AreasInteractor {
    override suspend fun getAreas(): Resource<List<AreaFilter>> {
        return repository.getAreas()
    }
}
