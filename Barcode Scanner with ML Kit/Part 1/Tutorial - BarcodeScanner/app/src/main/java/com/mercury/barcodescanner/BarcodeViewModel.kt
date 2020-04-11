package com.mercury.barcodescanner

import android.app.Application
import androidx.lifecycle.*
import com.mercury.barcodescanner.Room.Contact
import com.mercury.barcodescanner.Room.ContactDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarcodeViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var isDataPresent :LiveData<Int>
    private var contactDatabase: ContactDatabase = ContactDatabase.getInstance(context = this.getApplication())

    val contactData : LiveData<List<Contact>> = contactDatabase.contactDAO().getAllContacts()

    fun insertContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDatabase.contactDAO().insertContact(contact)
    }

    fun isAlreadyPresent(email: String): LiveData<Int> {
        isDataPresent = contactDatabase.contactDAO().checkIfAlreadyAvailable(email)
        return  isDataPresent
    }

}