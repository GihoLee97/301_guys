package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Setting(@PrimaryKey(autoGenerate = true)var id:Long?,
@ColumnInfo(name="volume")var volume: Int,
              @ColumnInfo(name="push")var push: Boolean,
              @ColumnInfo(name="autospeed")var autospeed: Int,
              @ColumnInfo(name="thema")var thema: String,
              @ColumnInfo(name="index")var index: MutableList<String>
)
{
    constructor(): this(null,5,false,5,"default", mutableListOf("nothing"))
}