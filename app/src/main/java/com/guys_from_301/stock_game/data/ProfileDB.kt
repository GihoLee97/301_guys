package com.guys_from_301.stock_game.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities =  [Profile::class], version =  3)
abstract class ProfileDB: RoomDatabase() {
    abstract  fun profileDao(): ProfileDao

    companion object{
        private var INSTANCE : ProfileDB? =null

        fun getInstace(context: Context): ProfileDB?{
            if (INSTANCE == null){
                synchronized(ProfileDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ProfileDB::class.java,
                        "profile.db")
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