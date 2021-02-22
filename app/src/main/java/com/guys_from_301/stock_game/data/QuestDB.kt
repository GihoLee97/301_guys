package com.guys_from_301.stock_game.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Quest::class], version = 3)
abstract class QuestDB:RoomDatabase() {
    abstract fun questDao(): QuestDao

    companion object{
        private var INSTANCE : QuestDB? = null

        fun getInstance(context: Context): QuestDB?{
            if(INSTANCE == null){
                synchronized(QuestDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        QuestDB::class.java,
                        "quest.db")
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