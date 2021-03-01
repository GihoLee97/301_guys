package com.guys_from_301.stock_game.data

import androidx.room.*


@Dao
interface NoticeDao {
    @Query("SELECT * FROM notice")
    fun getAll(): List<Notice>

    @Query("DELETE FROM Notice")
    fun deleteAll()

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(notice: Notice)

    @Update
    fun update(notice: Notice)
}