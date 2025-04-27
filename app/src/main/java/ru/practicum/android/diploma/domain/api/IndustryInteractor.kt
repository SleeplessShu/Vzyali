package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.main.Industry

interface IndustryInteractor {
    suspend fun getIndustries(): Resource<List<Industry>>
}
