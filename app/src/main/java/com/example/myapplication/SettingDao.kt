package com.example.myapplication

import androidx.room.Dao
import androidx.room.Query


@Dao
interface SettingDao {
    @Query("SELECT * FROM Setting")
    fun getAll()
}