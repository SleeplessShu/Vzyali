package ru.practicum.android.diploma.data.bd

class VacancyShortBDEntity(
    val vacancyId: String, // id вакансии
    val logoUrl: String?, // иконка компании (можно взять logo_urls["90"])
    val name: String, // название вакансии
    val areaName: String, // город
    val employerName: String, // название компании (employer.name)
    val salary: String?
)
