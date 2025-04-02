package com.example.starwars.data.film

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Query("SELECT * FROM Film")
    fun getAllFilmsFlow(): Flow<List<Film>>

    @Query("SELECT * FROM Film")
    fun getAllFilms(): List<Film>

    @Query("SELECT * FROM Film Where id = :id")
    fun getFilmById(id: String): Film?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Film>)

    @Query("DELETE FROM Film")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(items: List<Film>) {
        deleteAll()
        insertAll(items)
    }
}