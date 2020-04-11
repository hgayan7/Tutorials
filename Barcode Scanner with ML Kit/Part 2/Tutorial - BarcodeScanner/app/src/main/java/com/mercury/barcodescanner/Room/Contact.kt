package com.mercury.barcodescanner.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var uid : Int,
    @ColumnInfo(name= "email")
    var email: String,
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "organization")
    var organization : String,
    @ColumnInfo(name = "phone")
    var phone : String,
    @ColumnInfo(name = "url")
    var url : String
)