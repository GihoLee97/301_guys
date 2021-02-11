package com._301_guys.stock_game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item (@PrimaryKey(autoGenerate = true) var id: Long?,
@ColumnInfo(name = "name") var name : String,
@ColumnInfo(name = "effect") var effect : String,
@ColumnInfo(name = "count") var count: Int,
@ColumnInfo(name = "price") var price: Int
)
{
    constructor(): this(null,"-","-", 0,0)
}