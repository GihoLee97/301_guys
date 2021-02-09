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
@ColumnInfo(name = "profittot") var profittot: Float,
@ColumnInfo(name = "buyorsell") var buyorsell:String,
                       @ColumnInfo(name = "volume") var volume: Float,
                       @ColumnInfo(name = "price") var price: Float,
                       @ColumnInfo(name = "select") var select: Int,
                       @ColumnInfo(name = "quant") var quant: Int,
@ColumnInfo(name = "profityear") var profityear: Float,
@ColumnInfo(name = "quant1x") var quant1x: Int,
@ColumnInfo(name = "quant3x") var quant3x: Int,
@ColumnInfo(name = "quantinv1x") var quantinv1x: Int,
@ColumnInfo(name = "quantinv3x") var quantinv3x: Int,
@ColumnInfo(name = "aver1x") var aver1x: Float,
@ColumnInfo(name = "aver3x") var aver3x :Float ,
@ColumnInfo(name = "averinv1x") var averinv1x: Float,
@ColumnInfo(name = "averinv3x") var averinv3x: Float,
@ColumnInfo(name = "val1x") var val1x: Float,
@ColumnInfo(name = "val3x") var val3x: Float,
@ColumnInfo(name = "valinv1x") var valinv1x: Float,
@ColumnInfo(name = "valinv3x") var valinv3x :Float ,
@ColumnInfo(name = "tradecomtot") var tradecomtot: Float,
                       @ColumnInfo(name = "tradecom") var tradecom:Float,
@ColumnInfo(name = "dividendtot") var dividendtot: Float,
@ColumnInfo(name = "taxtot") var taxtot: Float,
@ColumnInfo(name = "item") var item:String,
@ColumnInfo(name = "item1count")var item1count: Int,
@ColumnInfo(name = "item2count")var item2count: Int,
@ColumnInfo(name = "item3count")var item3count:Int
)
{
    constructor() : this("", 0F,0F,0F,0F,0F,0F,"매매",0F,0F,0,0,0F,0,0,0,
            0,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,"",0,0,0)
}
