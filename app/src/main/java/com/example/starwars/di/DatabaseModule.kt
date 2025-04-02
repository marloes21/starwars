package com.example.starwars.di

import android.content.Context
import androidx.room.Room
import com.example.starwars.data.AppDatabase
import com.example.starwars.data.character.CharacterDao
import com.example.starwars.data.film.FilmDao
import com.example.starwars.data.spaceship.SpaceshipDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFilmDao(database: AppDatabase): FilmDao = database.filmDao()

    @Provides
    @Singleton
    fun provideCharacterDao(database: AppDatabase): CharacterDao = database.characterDao()

    @Provides
    @Singleton
    fun provideSpaceShipDao(database: AppDatabase): SpaceshipDao = database.spaceShipDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app")
            .fallbackToDestructiveMigration()
            .build()
    }
}