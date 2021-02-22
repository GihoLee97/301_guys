package com.guys_from_301.stock_game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quest(@PrimaryKey(autoGenerate = true) var id: Int,
                 @ColumnInfo(name = "theme") var theme: String,
                 @ColumnInfo(name = "questcontents") var questcontents: String,
                 @ColumnInfo(name = "achieve") var achievement: Int
) {
    constructor(): this(0,"","",0)
}