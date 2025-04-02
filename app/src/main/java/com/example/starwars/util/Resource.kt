package com.example.starwars.util


sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Failure(val error: ErrorType) : Resource<Nothing>
}

fun <T> Resource<T>.dataOrNull() = (this as? Resource.Success<T>)?.data

suspend fun <T, R> Resource<T>.map(transform: suspend (T) -> R) = when (this) {
    is Resource.Failure -> this
    is Resource.Success -> Resource.Success(transform(data))
}
