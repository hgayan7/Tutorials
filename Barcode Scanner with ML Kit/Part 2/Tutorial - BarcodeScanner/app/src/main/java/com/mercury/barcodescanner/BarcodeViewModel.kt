package com.mercury.barcodescanner

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.FirebaseDatabase
import com.mercury.barcodescanner.Room.Contact
import com.mercury.barcodescanner.Room.ContactDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarcodeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var isDataPresent :LiveData<Int>
    private lateinit var singleContact : LiveData<Contact>
    private var contactDatabase: ContactDatabase = ContactDatabase.getInstance(context = this.getApplication())

    var rootReference = FirebaseDatabase.getInstance().reference

    val contactData : LiveData<List<Contact>> = contactDatabase.contactDAO().getAllContacts()

    fun insertContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDatabase.contactDAO().insertContact(contact)
    }

    fun isAlreadyPresent(email: String): LiveData<Int> {
        isDataPresent = contactDatabase.contactDAO().checkIfAlreadyAvailable(email)
        return  isDataPresent
    }

    fun getSingleContact(email: String) : LiveData<Contact> {
        singleContact = contactDatabase.contactDAO().getContact(email = email)
        return singleContact
    }

    fun addDataToFirebase(contact: Contact) {
        rootReference.child("contacts")
            .child("${contact.uid}").setValue(contact)
    }

    fun deleteDataFromFirebase(contact: Contact) {
        rootReference.child("contacts")
            .child(contact.uid.toString()).removeValue()
    }

    fun deleteDataFromRoom(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDatabase.contactDAO().deleteContact(contact)
    }
}