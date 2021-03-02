package com.guys_from_301.stock_game.data

import androidx.room.*


@Dao
interface GameNormalDao {
    @Query("SELECT * FROM GameNormal WHERE buyorsell IS NOT '최종' ORDER BY id ASC")
    fun getAll(): List<GameNormal>

    @Query("SELECT assets FROM GameNormal WHERE buyorsell IS '자산 차트'")
    fun getChart(): List<Float>

    @Query("SELECT * FROM GameNormal WHERE setId = :setId AND buyorsell IS NOT '최종' ORDER BY id ASC")
    fun getSetWithNormal(setId: Int): List<GameNormal>

    @Query("SELECT max(endtime) FROM GameNormal WHERE setId = :setId AND buyorsell IS NOT '최종' ORDER BY id ASC")
    fun getSetWithNormalLastEndTime(setId: Int): String

    @Query("SELECT item1able FROM GameNormal WHERE setId = :setId AND buyorsell IS '저장' ORDER BY id ASC")
    fun getSetWithNormalItem1Able(setId: Int):  List<Int>

    @Query("SELECT profitrate FROM GameNormal WHERE setId = :setId AND buyorsell IS NOT '최종' ORDER BY id ASC")
    fun getSetWithNormalLastProfitRate(setId: Int): List<Float>

    @Query("SELECT profittot FROM GameNormal")
    fun getProfitTot(): Float

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(gameNormal: GameNormal)

    @Query("SELECT * FROM GameNormal WHERE buyorsell IS '매수' OR buyorsell IS '매도'")
    fun getHistory(): List<GameNormal>

    @Query("SELECT * FROM GameNormal WHERE buyorsell IS '종료'")
    fun getLast(): List<GameNormal>

    @Update
    fun update(gameNormal: GameNormal)

    @Query("SELECT id FROM GameNormal")
    fun getId(): List<String>

    @Query("DELETE from GameNormal")
    fun deleteAll()

    @Query("DELETE FROM GameNormal WHERE setId IS :id")
    fun deleteId(id: Int)

}