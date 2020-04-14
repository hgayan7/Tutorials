package com.mercury.barcodescanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mercury.barcodescanner.Room.Contact
import kotlinx.android.synthetic.main.contact_cell.view.*

class BarcodeAdapter(private val contacts : List<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_cell, parent, false)
        return ContactHolder(v)}

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ContactHolder ->{
                holder.bind(contacts[position])
            }
        }
    }

    class ContactHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(contact : Contact){
            itemView.name.text = contact.name
            itemView.organization.text = contact.organization
            itemView.website.text = contact.url
            itemView.phoneNo.text = contact.phone
        }
    }
}