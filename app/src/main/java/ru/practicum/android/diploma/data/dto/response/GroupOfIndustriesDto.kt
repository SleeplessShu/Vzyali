package ru.practicum.android.diploma.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.practicum.android.diploma.data.dto.main.IndustryDto

@Serializable
data class GroupOfIndustriesDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("industries") val industries: List<IndustryDto>
)
