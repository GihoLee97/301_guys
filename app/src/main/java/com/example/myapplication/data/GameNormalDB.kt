package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities =  [GameNormal::class], version = 3)
abstract class GameNormalDB:RoomDatabase() {
    abstract fun gameNormalDao(): GameNormalDao

    companion object {
        @Volatile
        private var INSTANCE: GameNormalDB? = null

        fun getInstance(context: Context): GameNormalDB {
        val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameNormalDB::class.java,
                    "gameNormalDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


        fun destroyINSTANCE(){
            INSTANCE = null
        }

    }

}