package com.example.starwars.di

import com.example.starwars.data.character.CharacterApi
import com.example.starwars.data.character.CharacterApiKtor
import com.example.starwars.data.film.FilmApi
import com.example.starwars.data.film.FilmApiKtor
import com.example.starwars.data.spaceship.SpaceshipApi
import com.example.starwars.data.spaceship.SpaceshipApiKtor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://swapi.dev/api/"

    @Provides
    @Singleton
    fun provideFilmApi(client: HttpClient): FilmApi {
        return FilmApiKtor(client,BASE_URL + "films")

    }

    @Provides
    @Singleton
    fun provideCharacterApi(client: HttpClient): CharacterApi {
        return CharacterApiKtor(client,BASE_URL + "people")

    }

    @Provides
    @Singleton
    fun provideSpaceShipApi(client: HttpClient): SpaceshipApi {
        return SpaceshipApiKtor(client,BASE_URL + "starships")

    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                contentType(ContentType.Application.Json)

            }

            //api calls can be slow, for now timeouts changed to 30 seconds
            install(HttpTimeout) {
                requestTimeoutMillis = 60000 // Set request timeout to 30 seconds
                connectTimeoutMillis = 60000 // Set connection timeout to 30 seconds
                socketTimeoutMillis = 60000 // Set socket timeout to 30 seconds
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        explicitNulls = false
                        ignoreUnknownKeys = true
                    }
                )
            }
            expectSuccess = true // throw exception when status code is not >= 200 && <300
        }
    }
}