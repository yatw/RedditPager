package com.example.redditpager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Post::class, PostKeys::class],
    version = 1,
    exportSchema = false
)
abstract class PagerDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun postKeysDao(): PostKeysDao

    companion object {

        @Volatile
        private var INSTANCE: PagerDatabase? = null

        fun getInstance(context: Context): PagerDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PagerDatabase::class.java, "PagerDatabase"
            ).build()
    }
}
