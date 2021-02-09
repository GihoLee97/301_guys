package com.example.myapplication.data

import androidx.room.*

@Dao
interface GameSetDao {
    @Query("SELECT * FROM gameset")
    fun getAll(): MutableList<GameSet>

    @Query("SELECT setcash FROM GameSet")
    fun getSetCash(): Float

    @Query("SELECT setmonthly FROM GameSet")
    fun getSetMonthly(): Float

    @Query("SELECT setsalaryraise FROM GameSet")
    fun getSetSalaryRaise(): Float

    @Query("SELECT setgamelength FROM GameSet")
    fun getSetGameLength(): Int

    @Query("SELECT setgamespeed FROM GameSet")
    fun getSetGameSpeed(): Int

    @Insert
    fun insert(gameSet: GameSet)

    @Update
    fun update(gameSet: GameSet)

    @Delete
    fun delete(gameSet: GameSet)
}