package com.febiarifin.storyappsubmissiondicoding.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem

@Database(
    entities = [StoryResponseItem::class],
    version = 1,
    exportSchema = false
)

abstract class StoryDatabase: RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object{
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}