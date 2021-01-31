package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface SettingDao {
    @Query("SELECT * FROM Setting")
    fun getAll(): MutableList<Setting>

    @Query("SELECT volume FROM Setting")
    fun getVolume(): Boolean

    @Query("SELECT push FROM Setting")
    fun getPush(): Boolean

    @Query("SELECT autospeed FROM Setting")
    fun getAutoSpeed(): Int

    @Query("SELECT thema FROM Setting")
    fun getThema(): Int

    @Query("SELECT `index` FROM Setting")
    fun getIndex(): Int

    @Insert
    fun insert(setting: Setting)

    @Update
    fun update(setting: Setting)

    @Query("DELETE FROM Setting")
    fun deleteAll()
}