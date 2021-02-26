package com.guys_from_301.stock_game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(@PrimaryKey(autoGenerate = true) var id: Long?,

    @ColumnInfo(name = "lasttime") var lasttime: Long // 피로도 저감 마지막 시점 저장


) {
    constructor() : this(null, 0L)
}