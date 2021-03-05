package com.guys_from_301.stock_game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guys_from_301.stock_game.RAISE_SET_CASH
import com.guys_from_301.stock_game.RAISE_SET_MONTHLY
import com.guys_from_301.stock_game.RAISE_SET_SALARY_RAISE

@Entity
data class GameSet(@PrimaryKey(autoGenerate = false ) var id: String,
//                   @ColumnInfo(name = "setname") var setname: String,
                   @ColumnInfo(name ="setcash") var setcash: Int,
                   @ColumnInfo(name="setmonthly") var setmonthly: Int,
                   @ColumnInfo(name = "setsalaryraise") var setsalaryraise: Int,
                   @ColumnInfo(name = "setgamelength") var setgamelength: Int,
                   @ColumnInfo(name = "setgamespeed") var setgamespeed: Int,
                   @ColumnInfo(name = "profitrate") var profitrate: Float,
                   @ColumnInfo(name = "endtime") var endtime: String,
                   @ColumnInfo(name = "accountId") var accountId: String

) {
    constructor(): this("",0,0,0,30,0,0F, "","")

}