package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.main.VacancyLongDto
import ru.practicum.android.diploma.data.dto.response.GroupOfIndustriesDto
import ru.practicum.android.diploma.data.dto.response.VacancySearchResponseDto

interface HeadHunterApi {

    @GET("/vacancies")
    suspend fun searchVacancies(@QueryMap filters: Map<String, String>): VacancySearchResponseDto

    @GET("/vacancies/{id}")
    suspend fun getVacancyDetails(@Path("id") id: String): VacancyLongDto

    @GET("/industries")
    suspend fun getIndustries(): List<GroupOfIndustriesDto>
}
