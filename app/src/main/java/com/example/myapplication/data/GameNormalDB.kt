package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.ProfileActivity

@Database(entities =  [GameNormal::class], version = 3)
abstract class GameNormalDB:RoomDatabase() {
    abstract fun gameNormalDao(): GameNormalDao

    companion object {
        private var INSTANCE : GameNormalDB? =null

        fun getInstace(context: Context): GameNormalDB?{
            if (INSTANCE == null){
                synchronized(GameNormalDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            GameNormalDB::class.java,
                            "gamenormal.db")
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