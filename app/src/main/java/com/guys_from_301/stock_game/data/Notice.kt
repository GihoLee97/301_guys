package com.guys_from_301.stock_game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notice(@PrimaryKey(autoGenerate = true) var id: Int,
                  @ColumnInfo(name= "title") var title: String,
                  @ColumnInfo(name = "contents") var contents: String,
                  @ColumnInfo(name = "date") var date: String

) {
    constructor(): this(0,"","","")
}