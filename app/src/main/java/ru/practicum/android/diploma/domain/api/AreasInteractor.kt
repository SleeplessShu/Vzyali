package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.domain.models.Resource

interface AreasInteractor {
    suspend fun getAreas(): Resource<List<AreaFilter>>
}
