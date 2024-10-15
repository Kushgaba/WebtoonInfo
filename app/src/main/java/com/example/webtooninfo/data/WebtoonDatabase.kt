package com.example.webtooninfo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WebtoonLocal::class], version = 1)
abstract class WebtoonDatabase : RoomDatabase(){
    abstract fun webtoonDao() : WebtoonDAO

    companion object {
        @Volatile
        private var INSTANCE: WebtoonDatabase? = null

        fun getDatabase(context: Context): WebtoonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WebtoonDatabase::class.java,
                    "webtoon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}