package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile")
    fun getAll(): MutableList<Profile>

    @Query("SELECT id FROM Profile")
    fun getId(): List<Int>

    @Query("SELECT nickname FROM Profile")
    fun getNickname(): List<String>

    @Query("SELECT profit FROM Profile")
    fun getProfit(): List<Int>

    @Query("SELECT History FROM Profile")
    fun getHistory(): List<String>

    @Query("SELECT level FROM Profile")
    fun getLevel(): List<Int>

    @Query("SELECT rank FROM Profile")
    fun getRank(): List<Int>

    @Query("SELECT login FROM Profile")
    fun getLogin(): String

    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)
}