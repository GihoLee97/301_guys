package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface QuestDao {
    @Query("SELECT * FROM Quest ORDER BY id ASC")
    fun getAll(): List<Quest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quest: Quest)

    @Update
    fun uqdate(quest: Quest)

    @Query("DELETE from Quest")
    fun deleteAll()
}