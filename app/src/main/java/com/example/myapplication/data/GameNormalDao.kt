package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GameNormalDao {
    @Query("SELECT * FROM GameNormal ORDER BY id ASC")
    fun getAll(): LiveData<List<GameNormal>>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(gameNormal: GameNormal)

    @Update
    fun update(gameNormal: GameNormal)

    @Query("DELETE from GameNormal")
    fun deleteAll()


}