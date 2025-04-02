package com.example.starwars.data.character

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface CharacterDao {

    @Query("SELECT * FROM Character")
    suspend fun getAll(): List<Character>

    @Query("SELECT * FROM Character")
    fun getAllFlow(): Flow<List<Character>>

    @Query("SELECT * FROM Character WHERE id = :id")
    suspend fun getById(id: String): Character?

    @Query("SELECT * FROM Character WHERE id = :id")
    fun getByIdFlow(id: String): Flow<Character?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Character)

    @Delete
    suspend fun delete(item: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Character>)

    @Query("DELETE FROM Character")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(items: List<Character>) {
        deleteAll()
        insertAll(items)
    }
}