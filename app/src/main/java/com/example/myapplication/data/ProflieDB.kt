package com.example.myapplication.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.ProfileActivity


@Database(entities =  [Profile::class], version =  3)
abstract class ProflieDB: RoomDatabase() {
    abstract  fun profileDao(): ProfileDao

    companion object{
        private var INSTANCE : ProflieDB? =null

        fun getInstace(context: ProfileActivity): ProflieDB?{
            if (INSTANCE == null){
                synchronized(ProflieDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ProflieDB::class.java,
                        "playlist.db")
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