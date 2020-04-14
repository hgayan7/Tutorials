package com.mercury.barcodescanner

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mercury.barcodescanner.Room.Contact
import com.mercury.barcodescanner.Room.ContactDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarcodeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var singleContact : LiveData<Contact>
    private var isPresent : MutableLiveData<Int> = MutableLiveData()
    private var contactDatabase: ContactDatabase = ContactDatabase.getInstance(context = this.getApplication())

    var rootReference = FirebaseDatabase.getInstance().reference

    val contactData : MutableLiveData<List<Contact>> = MutableLiveData()
    var firebaseContacts : List<Contact> = ArrayList()

    fun insertContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactDatabase.contactDAO().insertContact(contact)
    }

    fun isAlreadyPresentInFirebase(email: String) :MutableLiveData<Int> {
        rootReference.child("contacts").addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(s : DataSnapshot in snapshot.children){
                    if(s.child("email").value.toString() == email){
                        isPresent.value = -1
                        return
                    }
                }
                isPresent.value = 0
            }
        })
        return isPresent
    }

    fun getSingleContact(email: String) : LiveData<Contact> {
        singleContact = contactDatabase.contactDAO().getContact(email = email)
        return singleContact
    }

    fun getDataFromFirebase() :MutableLiveData<List<Contact>> {
        rootReference.child("contacts").addValueEventListener(object :ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseContacts = ArrayList()
                for(s : DataSnapshot in snapshot.children){
                    firebaseContacts += s.getValue(Contact::class.java)!!
                }
                contactData.postValue(firebaseContacts)
            }

        })
        return contactData
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