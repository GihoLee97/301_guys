package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GameNormalDao {
    @Query("SELECT * FROM GameNormal ORDER BY id ASC")
    fun getAll(): List<GameNormal>

    @Query("SELECT * FROM GameNormal WHERE buyorsell IS '매수' OR  buyorsell IS '매도' ORDER by id ASC")
    fun getHistory(): List<GameNormal>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(gameNormal: GameNormal)

    @Update
    fun update(gameNormal: GameNormal)

    @Query("SELECT id FROM GameNormal")
    fun getId(): List<String>

    @Query("DELETE from GameNormal")
    fun deleteAll()


}