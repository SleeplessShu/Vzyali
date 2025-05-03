package ru.practicum.android.diploma.data.impl

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.practicum.android.diploma.data.dto.additional.LocationDto
import ru.practicum.android.diploma.data.dto.mapper.toDomain
import ru.practicum.android.diploma.data.dto.mapper.toDto
import ru.practicum.android.diploma.data.storage.AppPrefsService
import ru.practicum.android.diploma.domain.models.FiltersState
import ru.practicum.android.diploma.domain.models.main.Location
import ru.practicum.android.diploma.domain.repositories.FiltersPrefsRepository

class FiltersPrefsRepoImpl(private val appPrefsService: AppPrefsService) : FiltersPrefsRepository {
    private val json = Json { encodeDefaults = true }

    override fun saveFilters(
        industryId: String?,
        industryName: String?,
        salary: Int?,
        hide: Boolean,
        location: Location?
    ) {
        if (location != null) {
            val dtoJson = json.encodeToString(location.toDto())
            appPrefsService.putString(KEY_LOCATION, dtoJson)
        } else {
            appPrefsService.remove(KEY_LOCATION)
        }
        if (!industryId.isNullOrBlank()) {
            appPrefsService.putString(KEY_INDUSTRY_ID, industryId)
        } else {
            appPrefsService.remove(KEY_INDUSTRY_ID)
        }
        if (!industryName.isNullOrBlank()) {
            appPrefsService.putString(KEY_INDUSTRY_NAME, industryName)
        } else {
            appPrefsService.remove(KEY_INDUSTRY_NAME)
        }
        if (salary != null) {
            appPrefsService.putString(KEY_SALARY, salary.toString())
        } else {
            appPrefsService.remove(KEY_SALARY)
        }
        appPrefsService.putString(KEY_HIDE_WITHOUT_SALARY, hide.toString())
    }

    override fun getFilters(): FiltersState {
        val rawLocJson = appPrefsService.getString(KEY_LOCATION)
        val location: Location? = rawLocJson
            ?.takeIf { it.isNotBlank() }
            ?.let { json.decodeFromString<LocationDto>(it) }
            ?.toDomain()
        val industryName = appPrefsService.getString(KEY_INDUSTRY_NAME)
        val industryId = appPrefsService.getString(KEY_INDUSTRY_ID)
        val salary = appPrefsService.getString(KEY_SALARY)?.toIntOrNull()
        val hide = appPrefsService.getString(KEY_HIDE_WITHOUT_SALARY).toBoolean()

        return FiltersState(
            industryId = industryId,
            industryName = industryName,
            salary = salary,
            hideWithoutSalary = hide,
            location = location
        )
    }

    override fun clearAll() {
        appPrefsService.remove(KEY_SALARY)
        appPrefsService.remove(KEY_INDUSTRY_NAME)
        appPrefsService.remove(KEY_INDUSTRY_ID)
        appPrefsService.remove(KEY_HIDE_WITHOUT_SALARY)
        appPrefsService.remove(KEY_LOCATION)
    }

    companion object {
        private const val KEY_INDUSTRY_ID = "industry_id"
        private const val KEY_INDUSTRY_NAME = "industry_name"
        private const val KEY_SALARY = "salary"
        private const val KEY_HIDE_WITHOUT_SALARY = "hide_without_salary"
        private const val KEY_LOCATION = "location"
    }
}
