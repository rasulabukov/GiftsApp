package com.example.giftsapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftsapp.db.AppDatabase
import com.example.giftsapp.db.ContactsEntity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ContactsFragment : Fragment(R.layout.fragment_contacts) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private var contacts: MutableList<ContactsEntity> = mutableListOf()
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_contacts)
        fab = view.findViewById(R.id.add_btn)

        recyclerView.layoutManager = LinearLayoutManager(context)
        contactAdapter = ContactAdapter(contacts, { contact -> deleteContact(contact) }, { contact -> sendGift(contact) })
        recyclerView.adapter = contactAdapter

        fab.setOnClickListener {
            showAddContactDialog()
        }

        loadContacts()
    }

    private fun loadContacts() {
        // Загрузка контактов из базы данных
        val db = AppDatabase.getDatabase(requireContext())
        Thread {
            contacts.clear()
            contacts.addAll(db.contactsDao().getAllContacts())
            requireActivity().runOnUiThread {
                contactAdapter.updateContacts(contacts)
            }
        }.start()
    }

    fun onContactAdded(contact: ContactsEntity) {
        contacts.add(contact)
        contactAdapter.notifyItemInserted(contacts.size - 1)
    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(requireContext())

        // Создаем View для диалога
        val viewInflated = LayoutInflater.from(context).inflate(R.layout.add_dialog, null)
        val inputName = viewInflated.findViewById<EditText>(R.id.editTextName)
        val addButton = viewInflated.findViewById<Button>(R.id.add_btn)
        val cancelButton = viewInflated.findViewById<Button>(R.id.canc_btn)

        builder.setView(viewInflated)

        // Создаем диалог
        val dialog = builder.create()

        // Устанавливаем действия на кнопки
        addButton.setOnClickListener {
            val name = inputName.text.toString()
            if (name.isNotBlank()) {
                val newContact = ContactsEntity(name = name)
                addContactToDatabase(newContact)
                dialog.dismiss() // Закрываем диалог после добавления контакта
            } else {
                Toast.makeText(requireContext(), "Введите имя контакта", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss() // Закрываем диалог без действий
        }

        dialog.show()
    }

    private fun addContactToDatabase(contact: ContactsEntity) {
        val db = AppDatabase.getDatabase(requireContext())
        Thread {
            db.contactsDao().insert(contact)
            requireActivity().runOnUiThread {
                contacts.add(contact)
                contactAdapter.notifyItemInserted(contacts.size - 1)
                Toast.makeText(requireContext(), "Контакт добавлен", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun deleteContact(contact: ContactsEntity) {
        val db = AppDatabase.getDatabase(requireContext())
        Thread {
            db.contactsDao().delete(contact) // Удаление контакта из базы
            requireActivity().runOnUiThread {
                contacts.remove(contact)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Контакт удален", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun sendGift(contact: ContactsEntity) {

        val intent = Intent(context, SelectGiftsActivity::class.java)
        intent.putExtra("CONTACT_ID", contact.id) // Передача ID контакта
        Log.d("ContactsFragment", "Sending gift for contact with ID: ${contact.id}")
        context?.startActivity(intent)
    }


}