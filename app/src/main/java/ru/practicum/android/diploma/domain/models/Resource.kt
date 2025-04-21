package ru.practicum.android.diploma.domain.models

sealed interface Resource<T> {
    data class Success<T>(val data: T, val pages: Int = 0) : Resource<T>
    data class Error<T>(val message: String) : Resource<T>
}
