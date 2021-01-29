package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile")
    fun getAll(): MutableList<Profile>

    @Query("SELECT id FROM Profile")
    fun getId(): Int

    @Query("SELECT nickname FROM Profile")
    fun getNickname(): String

    @Query("SELECT profit FROM Profile")
    fun getProfit(): Int

    @Query("SELECT History FROM Profile")
    fun getHistory(): String

    @Query("SELECT level FROM Profile")
    fun getLevel(): Int

    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)
}