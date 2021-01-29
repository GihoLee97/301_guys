package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile (@PrimaryKey(autoGenerate = true) var id: Long?,
@ColumnInfo(name="nickname") var nickname : String,
               @ColumnInfo(name="profit") var profit : Int,
               @ColumnInfo(name="history") var history : String,
               @ColumnInfo(name="level") var level : Int
)
{
    constructor(): this(null,"nickname",0,"no play",0)
}