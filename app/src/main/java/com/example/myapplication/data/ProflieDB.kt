package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.AccountManagementActivity
import com.example.myapplication.InitialActivity
import com.example.myapplication.ProfileActivity
import com.example.myapplication.SplashActivity


@Database(entities =  [Profile::class], version =  3)
abstract class ProflieDB: RoomDatabase() {
    abstract  fun profileDao(): ProfileDao

    companion object{
        private var INSTANCE : ProflieDB? =null

        fun getInstace(context: Context): ProflieDB?{
            if (INSTANCE == null){
                synchronized(ProflieDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ProflieDB::class.java,
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