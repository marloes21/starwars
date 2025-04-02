package com.example.starwars.util

suspend fun <T> sync(
    api: suspend () -> Resource<T>,
    dao: suspend (T) -> Unit,
) {
    when (val result = api()) {
        is Resource.Failure -> {
            // Handle failure
            println("API call failed: ${result.error}")
        }

        is Resource.Success -> {
            dao(result.data)
        }
    }
}