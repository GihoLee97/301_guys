package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities =  [Item::class], version =  1)
abstract  class ItemDB: RoomDatabase() {
    abstract  fun itemDao(): ItemDao

    companion object{
        private var INSTANCE : ItemDB? =null

        fun getInstace(context: Context): ItemDB?{
            if (INSTANCE == null){
                synchronized(ItemDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ItemDB::class.java,
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