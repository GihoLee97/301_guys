package com.guys_from_301.stock_game.data

import androidx.room.*


@Dao
interface GameNormalDao {
    @Query("SELECT * FROM GameNormal WHERE buyorsell IS NOT '최종' AND accountId = :accountID ORDER BY id ASC")
    fun getAll(accountID : String): List<GameNormal>

    @Query("SELECT assets FROM GameNormal WHERE buyorsell IS '자산 차트' AND accountId = :accountID AND  setId = :setId ORDER BY id ASC")
    fun getAsset(accountID : String, setId: String): List<Float>

    @Query("SELECT input FROM GameNormal WHERE buyorsell IS '자산 차트' AND accountId = :accountID AND  setId = :setId ORDER BY id ASC")
    fun getInput(accountID : String, setId: String): List<Float>

    @Query("SELECT * FROM GameNormal WHERE setId = :setId AND buyorsell IS NOT '최종' AND accountId = :accountID ORDER BY id ASC")
    fun getSetWithNormal(setId: String, accountID : String): List<GameNormal>

    @Query("SELECT max(endtime) FROM GameNormal WHERE setId = :setId AND buyorsell IS NOT '최종' AND accountId = :accountID ORDER BY id ASC")
    fun getSetWithNormalLastEndTime(setId: String, accountID : String): String

    @Query("SELECT item1able FROM GameNormal WHERE setId = :setId AND buyorsell IS '저장' AND accountId = :accountID ORDER BY id ASC")
    fun getSetWithNormalItem1Able(setId: String, accountID : String):  List<Int>

    @Query("SELECT profitrate FROM GameNormal WHERE setId = :setId AND buyorsell IS NOT '최종' AND accountId = :accountID ORDER BY id ASC")
    fun getSetWithNormalLastProfitRate(setId: String, accountID : String): List<Float>

    @Query("SELECT profittot FROM GameNormal WHERE accountId = :accountID")
    fun getProfitTot(accountID : String): Float

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(gameNormal: GameNormal)

    @Query("SELECT * FROM GameNormal WHERE buyorsell IS '매수' OR buyorsell IS '매도' AND accountId = :accountID")
    fun getHistory(accountID : String): List<GameNormal>

    @Query("SELECT * FROM GameNormal WHERE buyorsell IS '최종' AND accountId = :accountID")
    fun getLast(accountID : String): List<GameNormal>

    @Query("SELECT max(profitrate) FROM GameNormal WHERE buyorsell IS '최종' AND accountId = :accountID")
    fun getBestProfit(accountID : String): Float

    @Update
    fun update(gameNormal: GameNormal)

    @Query("DELETE FROM GameNormal")
    fun deleteSignOut()

    @Query("SELECT id FROM GameNormal WHERE accountId = :accountID")
    fun getId(accountID : String): List<String>

    @Query("DELETE from GameNormal WHERE accountId = :accountID")
    fun deleteAll(accountID : String)

    @Query("DELETE FROM GameNormal WHERE setId IS :id AND accountId = :accountID AND buyorsell IS NOT '최종'")
    fun deleteId(id: String, accountID : String)

}