package com.mercury.barcodescanner.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDAO {
    @Query("SELECT * FROM contact")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT COUNT(*) FROM contact WHERE email = :email")
    fun checkIfAlreadyAvailable(email : String) : LiveData<Int>

    @Query("SELECT * FROM contact WHERE email = :email")
    fun getContact(email: String) : LiveData<Contact>

    @Insert()
    suspend fun insertContact(contact: Contact)

    @Delete()
    suspend fun deleteContact(contact: Contact)
}