package com.example.starwars.domain

import com.example.starwars.data.character.Character
import com.example.starwars.data.character.CharacterRepository
import com.example.starwars.data.film.Film
import com.example.starwars.data.film.FilmRepository
import com.example.starwars.data.spaceship.Spaceship
import com.example.starwars.data.spaceship.SpaceshipRepository
import com.example.starwars.util.ErrorType
import com.example.starwars.util.Resource
import com.example.starwars.util.dataOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CombineFilmWithCharactersAndSpaceshipUseCase(
    private val filmRepository: FilmRepository,
    private val characterRepository: CharacterRepository,
    private val spaceShipRepository: SpaceshipRepository,
) {

    fun execute(filmId: String): Flow<Resource<CombinedFilmData>> = flow {
        val filmResource = filmRepository.getFilmById(filmId)
        when (filmResource) {
            is Resource.Success<*> -> {
                val film = filmResource.dataOrNull()
                if (film == null) {
                    emit(Resource.Failure(ErrorType.NoFilmsFound))
                } else {
                    val characters = getCharacters(film.characterIds)
                    val spaceShips = getSpaceShips(film.spaceShipsIds)
                    emit(Resource.Success(CombinedFilmData(film, characters, spaceShips)))
                }
            }

            is Resource.Failure -> {
                emit(Resource.Failure(filmResource.error))
            }
        }
    }

    private suspend fun getCharacters(ids: List<String>): List<Character> {
        return characterRepository.getCharactersByIds(ids)
    }

    private suspend fun getSpaceShips(ids: List<String>): List<Spaceship> {
        return spaceShipRepository.getSpaceshipsByIds(ids)
    }

}

data class CombinedFilmData(
    val film: Film,
    val characters: List<Character>,
    val spaceships: List<Spaceship>,
)
