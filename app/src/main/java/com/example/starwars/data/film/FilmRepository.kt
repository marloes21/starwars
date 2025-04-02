package com.example.starwars.data.film

import com.example.starwars.data.toNetworkError
import com.example.starwars.util.Resource
import com.example.starwars.util.map
import com.example.starwars.util.sync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


interface FilmRepository {
    fun getAllFilms(): Flow<List<Film>>
     fun syncFilms():Flow<Resource<List<Film>>>
    suspend fun getFilmById(id: String): Resource<Film>
    suspend fun toggleFavorite(film: Film)

}

class FilmRepositoryImpl(
    private val api: FilmApi,
    private val dao: FilmDao,
) : FilmRepository {

    override fun syncFilms(): Flow<Resource<List<Film>>>  = flow {
        val apiFilms = api.getAllFilms().map {
            it.films.map {
                it.toFilm()
            }
        }
        sync({ apiFilms }, dao::replaceAll)
        emit(apiFilms)
    }.catch {
        emit(Resource.Failure(it.toNetworkError()))
    }

    override fun getAllFilms(): Flow<List<Film>> {
        val films = dao.getAllFilmsFlow()
        return films
    }

    override suspend fun getFilmById(id: String): Resource<Film> {
        return withContext(Dispatchers.IO) {
            val localFilm = dao.getFilmById(id)
            if (localFilm != null) {
                Resource.Success(localFilm)
            } else {
                api.getFilmById(id).map { it.toFilm() }
            }
        }
    }

    override suspend fun toggleFavorite(film: Film) {
        val updatedFilm = film.copy(favorite = !film.favorite)
        dao.insert(updatedFilm)
    }
}