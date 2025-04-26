package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.mapper.toDomain
import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.utils.StringProvider
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.domain.repositories.IndustryRepository

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val stringProvider: StringProvider
) : IndustryRepository {
    override suspend fun getIndustries(): Resource<List<Industry>> {
        return when (val response = networkClient.getGroupsOfIndustries()) {
            is Response.Success -> {
                Resource.Success(
                    response.data
                        .flatMap { group ->
                            group.industries.map { it.toDomain() }
                        }
                        .sortedBy { it.name }
                )
            }

            is Response.NoConnection -> Resource.Error(
                stringProvider.getString(R.string.errors_No_connection)
            )

            else -> Resource.Error(
                stringProvider.getString(R.string.errors_Server)
            )
        }
    }
}
