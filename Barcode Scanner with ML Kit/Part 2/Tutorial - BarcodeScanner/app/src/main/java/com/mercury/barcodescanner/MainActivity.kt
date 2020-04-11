package com.mercury.barcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            startActivity(Intent(this,BarcodeScannerActivity::class.java))
        }
        isData = intent.getBooleanExtra("fromScannerActivity",false)
        getContactsData()
        if(isData){
            processData()
        }
    }
    private fun processData() {
        barcodeViewModel.isAlreadyPresent( intent.getStringExtra("email") ?: "NA").observe(this,
            Observer { emailCount ->
                emailCount?.let {
                    if(emailCount == 0) {
                        addDataToDatabases()
                    }
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
                barcodeViewModel.deleteDataFromFirebase(contactList[viewHolder.adapterPosition])
                barcodeViewModel.deleteDataFromRoom(contactList[viewHolder.adapterPosition])
            }
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun getContactsData() {
        barcodeViewModel.contactData.observe(this, Observer { contacts ->
            this.contactList = ArrayList(contacts)
            barcodeAdapter = BarcodeAdapter(contacts = contacts)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = barcodeAdapter
            handleSwipeToDelete()
        })
    }
}
