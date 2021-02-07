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
    fun getId(): Long

    @Query("SELECT nickname FROM Profile")
    fun getNickname(): String

    @Query("SELECT profit FROM Profile")
    fun getProfit(): Int

    @Query("SELECT History FROM Profile")
    fun getHistory(): String

    @Query("SELECT level FROM Profile")
    fun getLevel(): Int

    @Query("SELECT rank FROM Profile")
    fun getRank(): Int

    @Query("SELECT login FROM Profile")
    fun getLogin(): Int

    @Query("SELECT login_id FROM Profile")
    fun getLoginid(): String

    @Query("SELECT login_pw FROM Profile")
    fun getLoginpw(): String

    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)
}