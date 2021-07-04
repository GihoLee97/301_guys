package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface QuestDao {
    @Query("SELECT * FROM Quest ORDER BY id ASC")
    fun getAll(): List<Quest>

    @Query("SELECT * FROM Quest WHERE theme IS :theme ORDER BY id ASC")
    fun getQuestByTheme(theme: String): List<Quest>

    @Query("SELECT * FROM Quest WHERE theme IS :theme AND achievement IS 0 ORDER BY id ASC")
    fun getQuestToAdapterByTheme(theme: String): List<Quest>

    @Query("SELECT * FROM Quest WHERE id IS :id")
    fun getQusetById(id: Int): List<Quest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quest: Quest)

    @Update
    fun update(quest: Quest)

    @Query("DELETE FROM Quest")
    fun deleteSignOut()

    @Query("DELETE from Quest")
    fun deleteAll()
}