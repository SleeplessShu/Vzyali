package ru.practicum.android.diploma.domain.models.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val countryId: Int = -1,
    val countryName: String = "",
    val regionId: Int = -1,
    val regionName: String = ""
) : Parcelable
