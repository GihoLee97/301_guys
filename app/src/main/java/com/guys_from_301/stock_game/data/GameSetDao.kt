package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface GameSetDao {
    @Query("SELECT * FROM gameset WHERE accountId = :accountID")
    fun getAll(accountID : String): MutableList<GameSet>

    @Query("SELECT setcash FROM GameSet WHERE accountId = :accountID")
    fun getSetCash(accountID : String): Int

    @Query("SELECT id FROM GameSet WHERE accountId = :accountID")
    fun getId(accountID : String): Int

    @Query("SELECT * FROM gameset WHERE id IS :accountID1 OR id IS :accountID2 OR id IS :accountID3 AND accountId = :accountID ORDER BY endtime")
    fun getPick(accountID: String,accountID1 : String, accountID2 : String, accountID3 : String): List<GameSet>

    @Query("SELECT * FROM GameSet WHERE id = :id  AND accountId = :accountID")
    fun getSetWithId(id: String,accountID : String): GameSet

    @Query("SELECT setmonthly FROM GameSet WHERE accountId = :accountID")
    fun getSetMonthly(accountID : String): Int

    @Query("SELECT setsalaryraise FROM GameSet WHERE accountId = :accountID")
    fun getSetSalaryRaise(accountID : String): Int

    @Query("SELECT setgamelength FROM GameSet WHERE accountId = :accountID")
    fun getSetGameLength(accountID : String): Int

    @Query("SELECT setgamespeed FROM GameSet WHERE accountId = :accountID")
    fun getSetGameSpeed(accountID : String): Int

    @Query("DELETE from GameSet WHERE accountId = :accountID")
    fun deleteAll(accountID : String)

    @Query("DELETE FROM GameSet WHERE id IS :id  AND accountId = :accountID")
    fun deleteId(id: String, accountID : String)

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