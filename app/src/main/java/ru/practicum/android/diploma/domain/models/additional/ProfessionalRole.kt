package ru.practicum.android.diploma.domain.models.additional

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfessionalRole(
    val id: String,
    val name: String
) : Parcelable
