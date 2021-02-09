package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GameNormalDao {
    @Query("SELECT * FROM GameNormal ORDER BY id ASC")
    fun getAll(): List<GameNormal>

    @Query("SELECT profittot FROM GameNormal")
    fun getProfitTot(): Float

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(gameNormal: GameNormal)

    @Update
    fun update(gameNormal: GameNormal)

    @Query("SELECT id FROM GameNormal")
    fun getId(): List<String>

    @Query("DELETE from GameNormal")
    fun deleteAll()
}