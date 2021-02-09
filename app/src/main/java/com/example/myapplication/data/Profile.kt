package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(@PrimaryKey(autoGenerate = true) var id: Long?,
                   @ColumnInfo(name = "nickname") var nickname: String,
                   @ColumnInfo(name = "money") var money: Int,
                   @ColumnInfo(name = "profit") var profit: Int,
                   @ColumnInfo(name = "history") var history: String,
                   @ColumnInfo(name = "level") var level: Int,
                   @ColumnInfo(name = "exp") var exp : Int,
                   @ColumnInfo(name = "rank") var rank: Int,
                   @ColumnInfo(name = "login") var login: Int,
                   @ColumnInfo(name = "login_id") var login_id: String,
                   @ColumnInfo(name = "login_pw") var login_pw: String
) {
    constructor() : this(null, "nickname", 0, 0, "no play", 1,0,0, 0, "","")
}