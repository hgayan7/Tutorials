package com.mercury.barcodescanner.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Contact::class),version = 1,exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDAO() : ContactDAO
    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getInstance(context: Context): ContactDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(ContactDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}