package ru.practicum.android.diploma.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite_vacancies_table")
data class VacancyLongDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "vacancy_id")
    val vacancyId: String,

    @ColumnInfo(name = "company_logo", defaultValue = "empty")
    val logoUrl: String?,

    @ColumnInfo(name = "vacancy_name", defaultValue = "unknown")
    val name: String,

    @ColumnInfo(name = "area_name", defaultValue = "unknown")
    val areaName: String,

    @ColumnInfo(name = "company_name", defaultValue = "unknown")
    val employer: String,

    @ColumnInfo(name = "salary", defaultValue = "Зарплата не указана")
    val salary: String,

    @ColumnInfo(name = "postedAt", defaultValue = "unknown")
    val postedAt: String,

    @ColumnInfo(name = "date_of_adding")
    val createdAt: Long,

    @ColumnInfo(name = "vacancy_description")
    val description: String,

    @ColumnInfo(name = "key_skills")
    val keySkills: String,

    @ColumnInfo(name = "experience")
    val experience: String,

    @ColumnInfo(name = "employment_form")
    val employmentForm: String,

    @ColumnInfo(name = "schedule")
    val schedule: String,

    @ColumnInfo(name = "address")
    val address: String
)
