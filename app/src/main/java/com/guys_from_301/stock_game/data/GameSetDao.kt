package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface GameSetDao {
    @Query("SELECT * FROM gameset")
    fun getAll(): MutableList<GameSet>

    @Query("SELECT setcash FROM GameSet")
    fun getSetCash(): Int

    @Query("SELECT id FROM GameSet")
    fun getId(): Int

    @Query("SELECT * FROM gameset WHERE id > 0 ORDER BY endtime")
    fun getPick(): List<GameSet>

    @Query("SELECT * FROM GameSet WHERE id = :id")
    fun getSetWithId(id: Int): GameSet

    @Query("SELECT setmonthly FROM GameSet")
    fun getSetMonthly(): Int

    @Query("SELECT setsalaryraise FROM GameSet")
    fun getSetSalaryRaise(): Int

    @Query("SELECT setgamelength FROM GameSet")
    fun getSetGameLength(): Int

    @Query("SELECT setgamespeed FROM GameSet")
    fun getSetGameSpeed(): Int

    @Query("DELETE from GameSet")
    fun deleteAll()

    @Query("DELETE FROM GameSet WHERE id IS :id")
    fun deleteId(id: Int)

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(gameSet: GameSet)

    @Update
    fun update(gameSet: GameSet)

    @Delete
    fun delete(gameSet: GameSet)

//    @Transaction
//    @Query("SELECT * FROM gameset WHERE id = :setId")
//    fun getSetWithNormal(setId: Int): List<SetWithNormal>
}