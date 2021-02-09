package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [GameSet::class], version = 3)
abstract class GameSetDB:RoomDatabase() {
    abstract fun gameSetDao(): GameSetDao

    companion object {
        private var INSTANCE : GameSetDB? =null

        fun getInstace(context: Context): GameSetDB?{
            if (INSTANCE == null){
                synchronized(GameSetDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            GameSetDB::class.java,
                            "gameset.db")
                            .allowMainThreadQueries().fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }



        fun destroyINSTANCE(){
            INSTANCE = null
        }

    }

}