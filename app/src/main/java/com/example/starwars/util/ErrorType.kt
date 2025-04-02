package com.example.starwars.util

import com.example.starwars.R
import kotlin.math.E

sealed interface ErrorType {
    data object NoFilmsFound : ErrorType
    data object NoCharactersFound : ErrorType
    data object Unknown : ErrorType
}

sealed interface NetworkError : ErrorType {
        data object UnknownHost : NetworkError
        data object Ssl : NetworkError
        data class Redirect(val statusCode: Int) : NetworkError
        data class BadRequest(val statusCode: Int) : NetworkError
        data class ServerError(val statusCode: Int) : NetworkError
        data class Other(val statusCode: Int) : NetworkError
        data object Timeout : NetworkError
        data object Serialization : NetworkError
        data object Unknown : NetworkError
}

val ErrorType.resId: Int
    get() = when (this) {
        ErrorType.Unknown -> R.string.error_unknown
        ErrorType.NoFilmsFound -> R.string.error_films_not_found
        ErrorType.NoCharactersFound -> R.string.error_characters_not_found
        // NetworkError
        NetworkError.UnknownHost -> R.string.error_network_unknown_host
        NetworkError.Ssl -> R.string.error_network_ssl
        is NetworkError.Redirect -> R.string.error_network_redirect
        is NetworkError.BadRequest -> R.string.error_network_bad_request
        is NetworkError.ServerError -> R.string.error_network_server_error
        is NetworkError.Other -> R.string.error_network_other
        NetworkError.Serialization -> R.string.error_network_serialization
        NetworkError.Timeout -> R.string.error_network_timeout
        NetworkError.Unknown -> R.string.error_network_unknown
    }