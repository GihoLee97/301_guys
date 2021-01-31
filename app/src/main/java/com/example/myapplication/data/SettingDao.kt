package com.example.myapplication.data

import androidx.room.*


@Dao
interface SettingDao {
    @Query("SELECT * FROM Setting")
    fun getAll(): MutableList<Setting>

    @Query("SELECT volume FROM Setting WHERE id = 1 ")
    fun getVolume(): List<Boolean>

    @Query("SELECT push FROM Setting WHERE id = 1")
    fun getPush(): List<Boolean>

    @Query("SELECT autospeed FROM Setting WHERE id = 1")
    fun getAutoSpeed(): List<Int>

    @Query("SELECT thema FROM Setting WHERE id = 1")
    fun getThema(): List<Int>

    @Query("SELECT `index` FROM Setting WHERE id = 1")
    fun getIndex(): List<Int>

    @Query("SELECT id FROM Setting")
    fun getId(): List<Int>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(setting: Setting)

    @Update
    fun update(setting: Setting)

    @Query("DELETE from Setting")
    fun deleteAll()


}