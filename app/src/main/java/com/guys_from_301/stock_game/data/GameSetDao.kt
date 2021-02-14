package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface GameSetDao {
    @Query("SELECT * FROM gameset")
    fun getAll(): MutableList<GameSet>

    @Query("SELECT setcash FROM GameSet")
    fun getSetCash(): Float

    @Query("SELECT id FROM GameSet")
    fun getId(): Int

    @Query("SELECT * FROM GameSet WHERE id = :id")
    fun getSetWithId(id: String): GameSet

    @Query("SELECT setmonthly FROM GameSet")
    fun getSetMonthly(): Float

    @Query("SELECT setsalaryraise FROM GameSet")
    fun getSetSalaryRaise(): Float

    @Query("SELECT setgamelength FROM GameSet")
    fun getSetGameLength(): Int

    @Query("SELECT setgamespeed FROM GameSet")
    fun getSetGameSpeed(): Int

    @Query("DELETE from GameSet")
    fun deleteAll()

    @Query("DELETE FROM GameSet WHERE id IS :id")
    fun deleteId(id: String)

    @Insert
    fun insert(gameSet: GameSet)

    @Update
    fun update(gameSet: GameSet)

    @Delete
    fun delete(gameSet: GameSet)

//    @Transaction
//    @Query("SELECT * FROM gameset WHERE id = :setId")
//    fun getSetWithNormal(setId: Int): List<SetWithNormal>
}