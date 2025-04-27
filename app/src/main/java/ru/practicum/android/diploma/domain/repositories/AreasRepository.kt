package ru.practicum.android.diploma.domain.repositories

import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.domain.models.Resource

interface AreasRepository {
    suspend fun getAreas(): Resource<List<AreaFilter>>
}
