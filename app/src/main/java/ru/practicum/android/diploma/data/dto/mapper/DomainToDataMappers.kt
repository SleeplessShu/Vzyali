package ru.practicum.android.diploma.data.dto.mapper

import ru.practicum.android.diploma.data.dto.additional.LocationDto
import ru.practicum.android.diploma.domain.models.main.Location

fun Location.toDto(): LocationDto = LocationDto(
    countryId = this.countryId,
    countryName = this.countryName,
    regionId = this.regionId,
    regionName = this.regionName
)
