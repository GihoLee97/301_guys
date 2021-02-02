package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameNormal (@PrimaryKey(autoGenerate = true) var id : Long?,
@ColumnInfo(name = "assets") var assets: Float,
@ColumnInfo(name = "cash") var cash : Float,
@ColumnInfo(name = "evaluation") var evaluation:Float,
@ColumnInfo(name = "profit") var profit:Float
)
{
    constructor() : this(null, 0F,0F,0F,0F)
}