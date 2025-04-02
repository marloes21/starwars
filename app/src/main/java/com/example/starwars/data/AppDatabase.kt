package com.example.starwars.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.starwars.data.character.Character
import com.example.starwars.data.character.CharacterDao
import com.example.starwars.data.converters.LocalDateConverter
import com.example.starwars.data.converters.StringListConverter
import com.example.starwars.data.film.Film
import com.example.starwars.data.film.FilmDao
import com.example.starwars.data.spaceship.Spaceship
import com.example.starwars.data.spaceship.SpaceshipDao
import nl.nvwa.combiform.data.converters.LocalDateTimeConverter

@Database(
    entities = [Film::class, Character::class, Spaceship::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    LocalDateTimeConverter::class,
    LocalDateConverter::class,
    StringListConverter::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun characterDao(): CharacterDao
    abstract fun spaceShipDao(): SpaceshipDao

}