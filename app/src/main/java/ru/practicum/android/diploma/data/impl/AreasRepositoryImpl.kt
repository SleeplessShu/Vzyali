package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.mapper.toDomain
import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.utils.StringProvider
import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.repositories.AreasRepository

class AreasRepositoryImpl(
    private val networkClient: NetworkClient,
    private val stringProvider: StringProvider
) : AreasRepository {
    override suspend fun getAreas(): Resource<List<AreaFilter>> {
        return when (val response = networkClient.getAreas()) {
            is Response.Success -> {
                Resource.Success(
                    response.data
                        .map { it.toDomain() }
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
