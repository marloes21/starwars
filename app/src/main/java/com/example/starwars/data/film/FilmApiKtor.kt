package com.example.starwars.data.film

import com.example.starwars.data.KtorApi
import com.example.starwars.util.Resource
import io.ktor.client.HttpClient

class FilmApiKtor(
    client: HttpClient,
    baseUrl: String,
) : FilmApi, KtorApi(client, baseUrl) {
    override suspend fun getAllFilms(): Resource<FilmsDto> = get<FilmsDto>()
    override suspend fun getFilmById(id: String): Resource<FilmDto> = get<FilmDto>(url = id)
}