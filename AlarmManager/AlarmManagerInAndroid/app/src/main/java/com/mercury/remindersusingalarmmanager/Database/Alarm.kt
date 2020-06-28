package com.mercury.remindersusingalarmmanager.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Himshikhar Gayan.
 */

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var uid : Int = 0,
    @ColumnInfo(name= "timing")
    var timing: String = "",
    @ColumnInfo(name= "requestCode")
    var requestCode: Int = 0
)