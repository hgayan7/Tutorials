package com.mercury.remindersusingalarmmanager.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Alarm::class),version = 1,exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDAO() : AlarmDAO
    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getInstance(context: Context): AlarmDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(AlarmDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}