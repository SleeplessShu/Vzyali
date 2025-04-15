package ru.practicum.android.diploma.data.dto.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.practicum.android.diploma.data.dto.additional.AddressDto
import ru.practicum.android.diploma.data.dto.additional.ContactsDto
import ru.practicum.android.diploma.data.dto.additional.EmploymentDto
import ru.practicum.android.diploma.data.dto.additional.ExperienceDto
import ru.practicum.android.diploma.data.dto.additional.KeySkillDto
import ru.practicum.android.diploma.data.dto.additional.ProfessionalRoleDto
import ru.practicum.android.diploma.data.dto.additional.ScheduleDto

@Serializable
data class VacancyLongDtoEntity(
    @SerialName("id") val vacancyId: String,
    val name: String,
    val description: String,
    val salary: SalaryDto? = null,
    @SerialName("key_skills") val keySkills: List<KeySkillDto> = emptyList(),
    val area: AreaDto,
    val employer: EmployerDto,
    val experience: ExperienceDto,
    val employment: EmploymentDto,
    val schedule: ScheduleDto,
    val contacts: ContactsDto? = null,
    val address: AddressDto? = null,
    @SerialName("professional_roles") val professionalRoles: List<ProfessionalRoleDto> = emptyList(),
    val industries: List<IndustryDto> = emptyList(),
    @SerialName("published_at") val publishedAt: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("archived") val archived: Boolean
)
