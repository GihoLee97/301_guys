package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameNormal (@PrimaryKey(autoGenerate = false) var id : String,
@ColumnInfo(name = "assets") var assets: Float,
@ColumnInfo(name = "cash") var cash : Float,
@ColumnInfo(name = "purchase") var purchaseamount: Float,
@ColumnInfo(name = "evaluation") var evaluation:Float,
@ColumnInfo(name = "profit") var profit:Float,
@ColumnInfo(name = "buyorsell") var buyorsell:String,
@ColumnInfo(name = "volume") var volume: Float,
@ColumnInfo(name = "fees") var fees:Float,
@ColumnInfo(name = "item") var item:String,
@ColumnInfo(name = "item1count")var item1count: Int,
@ColumnInfo(name = "item2count")var item2count: Int,
@ColumnInfo(name = "item3count")var item3count:Int
)
{
    constructor() : this("", 0F,0F,0F,0F,0F,"매매",0F,0F, "",0,0,0)
}
