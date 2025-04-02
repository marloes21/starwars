package com.example.starwars.data.spaceship

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface SpaceshipDao {

    @Query("SELECT * FROM Spaceship")
    suspend fun getAll(): List<Spaceship>

    @Query("SELECT * FROM Spaceship")
    fun getAllFlow(): Flow<List<Spaceship>>

    @Query("SELECT * FROM Spaceship WHERE id = :id")
    suspend fun getById(id: String): Spaceship?

    @Query("SELECT * FROM Spaceship WHERE id = :id")
    fun getByIdFlow(id: String): Flow<Spaceship?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Spaceship)

    @Delete
    suspend fun delete(item: Spaceship)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Spaceship>)

    @Query("DELETE FROM Spaceship")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(items: List<Spaceship>) {
        deleteAll()
        insertAll(items)
    }
}