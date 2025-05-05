package ru.practicum.android.diploma.data.dto.additional

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val countryId: Int = -1,
    val countryName: String = "",
    val regionId: Int = -1,
    val regionName: String = ""
)
