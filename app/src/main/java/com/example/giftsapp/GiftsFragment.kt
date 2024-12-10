package com.example.giftsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftsapp.db.AppDatabase
import com.example.giftsapp.db.ContactsDao
import com.example.giftsapp.db.ContactsEntity
import com.example.giftsapp.db.GiftsDao
import com.example.giftsapp.db.GiftsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GiftsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var giftAdapter: AdapterCheckGifts
    private var gifts: List<GiftsEntity> = listOf()
    private var contacts: List<ContactsEntity> = listOf()
    private lateinit var giftsDao: GiftsDao
    private lateinit var contactsDao: ContactsDao
    private var contactId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gifts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_gifts)
        recyclerView.layoutManager = GridLayoutManager(context, 1)


        // Получаем contactId, переданный через аргументы
        contactId = arguments?.getInt("CONTACT_ID") ?: 0

        // Инициализация DAO
        giftsDao = AppDatabase.getDatabase(requireContext()).giftDao()
        contactsDao = AppDatabase.getDatabase(requireContext()).contactsDao()

        // Загрузка данных
        loadGifts()
    }

    private fun loadGifts() {
        CoroutineScope(Dispatchers.IO).launch {
            val contactsList = contactsDao.getAllContacts()
            val giftsList = giftsDao.getAllGifts()

            withContext(Dispatchers.Main) {
                contacts = contactsList
                gifts = giftsList

                // Создаем и устанавливаем адаптер
                giftAdapter = AdapterCheckGifts(gifts, contacts) { giftId ->
                    // Обработчик клика по подарку
                }
                recyclerView.adapter = giftAdapter
                giftAdapter.notifyDataSetChanged()
            }
        }
    }

}
