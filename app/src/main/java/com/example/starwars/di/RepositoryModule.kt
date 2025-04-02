package com.example.starwars.di

import com.example.starwars.data.character.CharacterApi
import com.example.starwars.data.character.CharacterDao
import com.example.starwars.data.character.CharacterRepository
import com.example.starwars.data.character.CharacterRepositoryImpl
import com.example.starwars.data.film.FilmApi
import com.example.starwars.data.film.FilmDao
import com.example.starwars.data.film.FilmRepository
import com.example.starwars.data.film.FilmRepositoryImpl
import com.example.starwars.data.spaceship.SpaceshipApi
import com.example.starwars.data.spaceship.SpaceshipDao
import com.example.starwars.data.spaceship.SpaceshipRepository
import com.example.starwars.data.spaceship.SpaceshipRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFilmRepository(
        api: FilmApi,
        dao: FilmDao
    ): FilmRepository {
        return FilmRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        api: CharacterApi,
        dao: CharacterDao
    ): CharacterRepository {
        return CharacterRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideSpaceShipRepository(
        api: SpaceshipApi,
        dao: SpaceshipDao
    ): SpaceshipRepository {
        return SpaceshipRepositoryImpl(api, dao)
    }
}