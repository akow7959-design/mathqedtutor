package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LessonTask::class,
        HomeworkItem::class,
        StudentProfile::class,
        ChatMessage::class,
        LessonScheduleItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VimboxDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: VimboxDatabase? = null

        fun getDatabase(context: Context): VimboxDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VimboxDatabase::class.java,
                    "vimbox_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
