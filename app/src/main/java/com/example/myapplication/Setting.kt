package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(@PrimaryKey(autoGenerate = true)var id:Long?,
@ColumnInfo(name="volume")var volume: Boolean,
              @ColumnInfo(name="push")var push: Boolean,
              @ColumnInfo(name="autospeed")var autospeed: Int,
              @ColumnInfo(name="thema")var thema: Boolean,
              @ColumnInfo(name="index")var index: String
)
{
    constructor(): this(null,true,false,5,true, "-")
}