package com.example.starwars.data.film

import com.example.starwars.util.Resource

interface FilmApi {
    suspend fun getAllFilms() : Resource<FilmsDto>

    suspend fun getFilmById(id: String): Resource<FilmDto>
}