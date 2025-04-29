package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.storage.AppPrefsService
import ru.practicum.android.diploma.domain.models.FiltersState
import ru.practicum.android.diploma.domain.repositories.FiltersPrefsRepository

class FiltersPrefsRepoImpl(private val appPrefsService: AppPrefsService) : FiltersPrefsRepository {
    override fun saveFilters(
        industryId: String?,
        industryName: String?,
        salary: Int?,
        hide: Boolean
    ) {
        appPrefsService.putString(KEY_INDUSTRY_ID, industryId.orEmpty())
        appPrefsService.putString(KEY_INDUSTRY_NAME, industryName.orEmpty())
        appPrefsService.putString(KEY_SALARY, salary?.toString() ?: "")
        appPrefsService.putString(KEY_HIDE_WITHOUT_SALARY, hide.toString())
    }

    override fun getFilters(): FiltersState {
        val industryName = appPrefsService.getString(KEY_INDUSTRY_NAME)
        val industryId = appPrefsService.getString(KEY_INDUSTRY_ID)
        val salary = appPrefsService.getString(KEY_SALARY)?.toIntOrNull()
        val hide = appPrefsService.getString(KEY_HIDE_WITHOUT_SALARY).toBoolean()

        return FiltersState(
            industryId = industryId,
            industryName = industryName,
            salary = salary,
            hideWithoutSalary = hide
        )
    }

    override fun clearAll() {
        appPrefsService.remove(KEY_SALARY)
        appPrefsService.remove(KEY_INDUSTRY_NAME)
        appPrefsService.remove(KEY_INDUSTRY_ID)
        appPrefsService.remove(KEY_HIDE_WITHOUT_SALARY)
    }

    companion object {
        private const val KEY_INDUSTRY_ID = "industry_id"
        private const val KEY_INDUSTRY_NAME = "industry_name"
        private const val KEY_SALARY = "salary"
        private const val KEY_HIDE_WITHOUT_SALARY = "hide_without_salary"
        private const val KEY_AREA = "area"
    }
}
