package com.guys_from_301.stock_game.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Notice::class], version = 3)
abstract class NoticeDB:RoomDatabase() {
    abstract fun noticeDao(): NoticeDao

    companion object {
        private var INSTANCE : NoticeDB? =null

        fun getInstace(context: Context): NoticeDB?{
            if (INSTANCE == null){
                synchronized(NoticeDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NoticeDB::class.java,
                        "notice.db")
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