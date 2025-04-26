package ru.practicum.android.diploma.domain.repositories

import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.main.Industry

interface IndustryRepository {
    suspend fun getIndustries(): Resource<List<Industry>>
}
