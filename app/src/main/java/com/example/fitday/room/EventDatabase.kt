package com.example.fitday.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 1)
abstract class EventDatabase : RoomDatabase(){
    abstract val eventsDao: EventsDao

    companion object {
        fun create(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            EventDatabase::class.java,
            "FitDayDb.db"
        ).build()
    }
}