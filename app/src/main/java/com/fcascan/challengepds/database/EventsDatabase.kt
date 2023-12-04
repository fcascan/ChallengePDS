package com.fcascan.challengepds.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fcascan.challengepds.entities.EventEntity
import com.fcascan.challengepds.dao.EventsDAO

@Database(entities = [EventEntity::class], version = 2, exportSchema = false)
abstract class EventsDatabase: RoomDatabase() {
    // https://developer.android.com/codelabs/android-room-with-a-view-kotlin?hl=es-419#0
    abstract fun eventsDao(): EventsDAO
    companion object {
        private const val _TAG = "FCC#EventsDatabase"

        @Volatile
        private var INSTANCE: EventsDatabase? = null
        private const val DATABASE_NAME = "events.db"
        fun getDatabase(context: Context): EventsDatabase {
            if (INSTANCE == null) {
                Log.d("$_TAG - getDatabase", "Creating Room Database")
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        EventsDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }
}