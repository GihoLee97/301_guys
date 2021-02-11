package com._301_guys.stock_game.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Setting::class],version = 1)
abstract class SettingDB: RoomDatabase() {
    abstract fun settingDao(): SettingDao

    companion object{
        private  var INSTANCE: SettingDB? = null

        fun getInstace(context: Context): SettingDB?{
            if (INSTANCE == null){
                synchronized(SettingDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SettingDB::class.java,
                        "settting.db")
                        .allowMainThreadQueries().fallbackToDestructiveMigration()
                        .build()


                }
            }
            return INSTANCE
        }


        fun destroyINSTACE(){
            INSTANCE = null
        }
    }
}