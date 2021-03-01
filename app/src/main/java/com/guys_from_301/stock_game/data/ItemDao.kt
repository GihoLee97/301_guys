package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): MutableList<Item>

    @Query("SELECT id FROM Item")
    fun getId(): Long

    @Query("SELECT lasttime FROM Item")
    fun getLasttime(): Long

    @Query("SELECT potion FROM Item")
    fun getPotion(): Int

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)



}