package com.guys_from_301.stock_game.data

import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile")
    fun getAll(): MutableList<Profile>

    @Query("SELECT id FROM Profile")
    fun getId(): Long

    @Query("SELECT nickname FROM Profile")
    fun getNickname(): String

    @Query("SELECT money FROM Profile")
    fun getMoney(): Int

    @Query("SELECT value1 FROM Profile")
    fun getValue1(): Int

    @Query("SELECT profitrate FROM Profile")
    fun getProfitRate(): Float

    @Query("SELECT relativeprofitrate FROM Profile")
    fun getRelativeProfitRate(): Float

    @Query("SELECT roundcount FROM Profile")
    fun getRoundCount(): Int

    @Query("SELECT History FROM Profile")
    fun getHistory(): Int

    @Query("SELECT level FROM Profile")
    fun getLevel(): Int

    @Query("SELECT exp FROM Profile")
    fun getExp(): Int

    @Query("SELECT rank FROM Profile")
    fun getRank(): Int

    @Query("SELECT login FROM Profile")
    fun getLogin(): Int

    @Query("SELECT login_id FROM Profile")
    fun getLoginid(): String

    @Query("SELECT login_pw FROM Profile")
    fun getLoginpw(): String

    @Query("SELECT imageNum FROM PROFILE")
    fun getImageNum(): Int

    @Query("SELECT hash FROM PROFILE")
    fun getHash(): String

    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Delete
    fun delete(profile: Profile)
}