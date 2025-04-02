package com.example.starwars.di

import com.example.starwars.data.character.CharacterRepository
import com.example.starwars.data.film.FilmRepository
import com.example.starwars.data.spaceship.SpaceshipRepository
import com.example.starwars.domain.CombineFilmWithCharactersAndSpaceshipUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCombineFilmWithCharactersAndSpaceShipUseCase(
        filmRepository: FilmRepository,
        characterRepository: CharacterRepository,
        spaceShipRepository: SpaceshipRepository,
    ): CombineFilmWithCharactersAndSpaceshipUseCase {
        return CombineFilmWithCharactersAndSpaceshipUseCase(
            filmRepository,
            characterRepository,
            spaceShipRepository
        )
    }

}