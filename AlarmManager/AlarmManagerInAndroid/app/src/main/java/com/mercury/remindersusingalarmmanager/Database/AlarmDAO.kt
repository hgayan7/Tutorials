package com.mercury.remindersusingalarmmanager.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by Himshikhar Gayan.
 */

@Dao
interface AlarmDAO {
    @Query("SELECT * FROM alarm")
    fun getAllAlarms(): LiveData<List<Alarm>>

    @Insert()
    suspend fun insertAlarm(alarm: Alarm)
}