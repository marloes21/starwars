package com.example.starwars.data

import com.example.starwars.util.NetworkError
import com.example.starwars.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


open class KtorApi(
    val client: HttpClient,
    val baseUrl: String,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend inline fun <reified T> get(
        url: String = "",
        crossinline block: HttpRequestBuilder.() -> Unit = {},
    ) = request<T>(HttpMethod.Get, url, block)

    suspend inline fun <reified T> request(
        method: HttpMethod,
        url: String,
        crossinline block: HttpRequestBuilder.() -> Unit = {},
    ) = withContext(dispatcher) {
        try {
            Resource.Success(client.request("$baseUrl/$url") {
                this.method = method
                block()
            }.body<T>())
        } catch (t: Throwable) {
            Resource.Failure(t.toNetworkError())
        }
    }


}

fun Throwable.toNetworkError() = when (this) {
    is UnknownHostException -> NetworkError.UnknownHost
    is SSLHandshakeException -> NetworkError.Ssl
    is RedirectResponseException -> NetworkError.Redirect(response.status.value) // 3xx
    is ClientRequestException -> NetworkError.BadRequest(response.status.value) // 4xx
    is ServerResponseException -> NetworkError.ServerError(response.status.value) // 5xx
    is ResponseException -> NetworkError.Other(response.status.value)
    is HttpRequestTimeoutException -> NetworkError.Timeout
    is JsonConvertException -> NetworkError.Serialization
    else -> NetworkError.Unknown
}

