package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameSet(@PrimaryKey(autoGenerate = true) var id: Int,
                   @ColumnInfo(name ="setcash") var setcash: Float,
                   @ColumnInfo(name="setmonthly") var setmonthly: Float,
                   @ColumnInfo(name = "setsalaryraise") var setsalaryraise: Float,
                   @ColumnInfo(name = "setgamelength") var setgamelength: Int,
                   @ColumnInfo(name = "setgamespeed") var setgamespeed: Int

) {
    constructor(): this(0,0F,0F,0F,0,0)
}