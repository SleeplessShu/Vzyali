package ru.practicum.android.diploma.data.impl

import android.util.Log
import androidx.sqlite.SQLiteException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.db.VacancyLongDbEntity
import ru.practicum.android.diploma.domain.models.ResponseDb
import ru.practicum.android.diploma.domain.models.additional.Address
import ru.practicum.android.diploma.domain.models.additional.Employment
import ru.practicum.android.diploma.domain.models.additional.Experience
import ru.practicum.android.diploma.domain.models.additional.KeySkill
import ru.practicum.android.diploma.domain.models.additional.Schedule
import ru.practicum.android.diploma.domain.models.main.Employer
import ru.practicum.android.diploma.domain.models.main.LogoUrls
import ru.practicum.android.diploma.domain.models.main.Salary
import ru.practicum.android.diploma.domain.models.main.VacancyLong
import ru.practicum.android.diploma.domain.repositories.RepositoryFavoriteVacancies

class RepositoryFavoriteVacanciesImpl(
    private val vacancyDao: VacancyDao,
) : RepositoryFavoriteVacancies {

    override suspend fun insertVacancy(vacancy: VacancyLong): Result<Unit> {
        return try {
            val dataEntity = domainToData(vacancy)
            vacancyDao.insertVacancy(dataEntity)
            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    override suspend fun getAllVacancies(): List<VacancyLong> {
        val vacanciesDataList = vacancyDao.getAll()
        val vacanciesDomainList = vacanciesDataList.map { dataToDomain(it) }
        return vacanciesDomainList
    }

    override suspend fun getById(vacancyId: Int): Result<VacancyLong> {
        return try {
            val vacancyData = vacancyDao.getById(vacancyId)
            if (vacancyData != null) {
                Result.success(dataToDomain(vacancyData))
            } else {
                Result.failure(Exception("Вакансия не найдена"))
            }
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    override suspend fun deleteById(vacancyId: Int): Result<Unit> {
        return try {
            vacancyDao.deleteById(vacancyId)
            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    override fun getVacanciesFlow(): Flow<ResponseDb<List<VacancyLong>>> = flow {
        emit(ResponseDb.Loading.cast<List<VacancyLong>>())
        try {
            vacancyDao.getAllFlow()
                .collect { list ->
                    val domainList = list
                        .sortedByDescending { it.createdAt }
                        .map { dataToDomain(it) }
                    emit(ResponseDb.Success(domainList))
                }
        } catch (e: SQLiteException) {
            emit(ResponseDb.Error(e))
        }
    }

    override suspend fun clearDatabase(): Result<Unit> {
        return try {
            vacancyDao.clear()
            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    private fun domainToData(vacancy: VacancyLong): VacancyLongDbEntity {
        Log.d("logoURL", "domainToData: ${vacancy.logoUrl?.logo90}")
        val gson = Gson()
        return VacancyLongDbEntity(
            vacancyId = vacancy.vacancyId,
            logoUrl = vacancy.logoUrl?.logo90,
            name = vacancy.name,
            areaName = vacancy.areaName,
            employer = gson.toJson(vacancy.employer),
            salary = salaryToString(vacancy.salary),
            postedAt = vacancy.publishedAt,
            createdAt = System.currentTimeMillis(),
            description = vacancy.description,
            keySkills = gson.toJson(vacancy.keySkills),
            experience = gson.toJson(vacancy.experience),
            employmentForm = gson.toJson(vacancy.employmentForm),
            schedule = gson.toJson(vacancy.schedule),
            address = gson.toJson(vacancy.address)
        )
    }

    private fun dataToDomain(vacancy: VacancyLongDbEntity): VacancyLong {
        Log.d("logoURL", "domainToData: ${LogoUrls(logo90 = vacancy.logoUrl)}")
        val gson = Gson()
        return VacancyLong(
            vacancyId = vacancy.vacancyId,
            logoUrl = LogoUrls(logo90 = vacancy.logoUrl),
            name = vacancy.name,
            areaName = vacancy.areaName,
            employer = gson.fromJson(vacancy.employer, Employer::class.java),
            salary = stringToSalary(vacancy.salary),
            publishedAt = vacancy.postedAt,
            keySkills = gson.fromJson(vacancy.keySkills, object : TypeToken<List<KeySkill>>() {}.type),
            createdAt = vacancy.createdAt.toString(),
            description = vacancy.description,
            experience = gson.fromJson(vacancy.experience, Experience::class.java),
            employmentForm = gson.fromJson(vacancy.employmentForm, Employment::class.java),
            schedule = gson.fromJson(vacancy.schedule, Schedule::class.java),
            address = gson.fromJson(vacancy.address, Address::class.java)
        )
    }

    private fun salaryToString(input: Salary?): String {
        if (input == null) return "null*null*null*null"

        val from = input.from?.toString() ?: "null"
        val to = input.to?.toString() ?: "null"
        val currency = input.currency ?: "null"
        val gross = input.gross?.toString() ?: "null"

        return "$from*$to*$currency*$gross"
    }

    private fun stringToSalary(input: String): Salary {
        val parts = input.split("*")

        val from = parts.getOrNull(INDEX_FROM)?.takeIf { it != "null" }?.toIntOrNull()
        val to = parts.getOrNull(INDEX_TO)?.takeIf { it != "null" }?.toIntOrNull()
        val currency = parts.getOrNull(INDEX_CURRENCY)?.takeIf { it != "null" }
        val gross = parts.getOrNull(INDEX_GROSS)?.takeIf { it != "null" }?.toBooleanStrictOrNull()

        return Salary(from, to, currency, gross)
    }

    private companion object {
        const val INDEX_FROM = 0
        const val INDEX_TO = 1
        const val INDEX_CURRENCY = 2
        const val INDEX_GROSS = 3
    }
}
