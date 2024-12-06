package com.example.giftsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.giftsapp.db.ContactsEntity

class ContactAdapter(
    private var contacts: List<ContactsEntity>,
    private val onDeleteClick: (ContactsEntity) -> Unit,
    private val onGiftClick: (ContactsEntity) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactName: TextView = itemView.findViewById(R.id.contactName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_btn)
        val giftButton: ImageButton = itemView.findViewById(R.id.gift_btn)

        fun bind(contact: ContactsEntity) {
            contactName.text = contact.name
            deleteButton.setOnClickListener { onDeleteClick(contact) }
            giftButton.setOnClickListener { onGiftClick(contact) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contacts, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    fun updateContacts(newContacts: List<ContactsEntity>) {
        contacts = newContacts
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return contacts.size
    }
}
