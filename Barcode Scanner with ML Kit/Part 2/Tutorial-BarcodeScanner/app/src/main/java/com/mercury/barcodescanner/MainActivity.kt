package com.mercury.barcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mercury.barcodescanner.Room.Contact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isData = false
    private lateinit var barcodeViewModel:BarcodeViewModel
    private lateinit var barcodeAdapter:BarcodeAdapter
    private var contactList = ArrayList<Contact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barcodeViewModel = ViewModelProviders.of(this).get(BarcodeViewModel::class.java)
        scanBarcodeButton.setOnClickListener {
            val intent = Intent(this,BarcodeScannerActivity::class.java)
            startActivity(intent)
        }
        isData = intent.getBooleanExtra("fromScannerActivity",false)
        getContactsData()
        if(isData){
            processData()
        }
    }
    private fun processData() {
        barcodeViewModel.isAlreadyPresentInFirebase(intent.getStringExtra("email") ?: "NA").observe(this,
            Observer { isPresent->
                Log.d("Data", isPresent.toString())
                if (isPresent == 0){
                    addDataToDatabases()
                }else if(isPresent == -1){
                    Toast.makeText(this,"Contact email already present in database",Toast.LENGTH_LONG).show()
                } else {

                }
            })
    }

    private fun addDataToDatabases() {
        val contact = Contact(
            uid = 0, name = intent.getStringExtra("name") ?: "NA",
            email = intent.getStringExtra("email") ?: "NA",
            organization = intent.getStringExtra("organization") ?: "NA",
            url = intent.getStringExtra("url") ?: "NA",
            phone = intent.getStringExtra("phoneNo") ?: "NA"
        )
        barcodeViewModel.insertContact(contact = contact)
        barcodeViewModel.getSingleContact(email = contact.email).observe(this, Observer {roomContact ->
            roomContact?.let {
                barcodeViewModel.getSingleContact(email = contact.email).removeObservers(this)
                barcodeViewModel.addDataToFirebase(contact = roomContact)
            }
        })
    }

    private fun handleSwipeToDelete() {
        val touchCallback = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val swipedContact = contactList[viewHolder.adapterPosition]
                barcodeViewModel.deleteDataFromRoom(swipedContact)
                barcodeViewModel.deleteDataFromFirebase(swipedContact)
            }
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun getContactsData() {
        barcodeViewModel.getDataFromFirebase().observe(this, Observer {contacts ->
            this.contactList = ArrayList(contacts)
            barcodeAdapter = BarcodeAdapter(contacts = contacts)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = barcodeAdapter
            handleSwipeToDelete()
        })
    }
}
