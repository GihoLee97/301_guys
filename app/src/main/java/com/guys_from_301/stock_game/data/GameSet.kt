package com.guys_from_301.stock_game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameSet(@PrimaryKey(autoGenerate = false ) var id: Int,
//                   @ColumnInfo(name = "setname") var setname: String,
                   @ColumnInfo(name ="setcash") var setcash: Float,
                   @ColumnInfo(name="setmonthly") var setmonthly: Float,
                   @ColumnInfo(name = "setsalaryraise") var setsalaryraise: Float,
                   @ColumnInfo(name = "setgamelength") var setgamelength: Int,
                   @ColumnInfo(name = "setgamespeed") var setgamespeed: Int

) {
    constructor(): this(0,0F,0F,0F,0,0)
}