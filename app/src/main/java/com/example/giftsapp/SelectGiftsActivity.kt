package com.example.giftsapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.giftsapp.db.AppDatabase
import com.example.giftsapp.db.GiftImageDao
import com.example.giftsapp.db.GiftsDao
import com.example.giftsapp.db.GiftsEntity
import com.example.giftsapp.db.GiftsImageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectGiftsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var giftAdapter: GiftAdapter
    private lateinit var giftImageDao: GiftImageDao
    private lateinit var giftDao: GiftsDao
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var contactId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_gifts)
        val back: ImageButton = findViewById(R.id.back)
        giftImageDao = AppDatabase.getDatabase(this).giftImageDao()
        giftDao = AppDatabase.getDatabase(this).giftDao()

        // Инициализация компонента SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.refresh)

        recyclerView = findViewById(R.id.cont_select_gifts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        contactId = intent.getIntExtra("CONTACT_ID", 0)
        Log.d("Activity", "Sending gift for contact with ID: $contactId")

        // Сохраняем изображения в базу данных
        saveGiftImages()

        // Загружаем изображения из базы данных
        loadGiftImages()

        // Настройка обработчика для кнопки "назад"
        back.setOnClickListener {
            finish()
        }

        // Настройка обработчика для обновления
        swipeRefreshLayout.setOnRefreshListener {
            saveGiftImages()
            loadGiftImages() // Перезагружаем подарки
        }
    }

    private fun saveGiftImages() {
        val giftImages = listOf(
            R.drawable.gift1, R.drawable.gift2, R.drawable.gift3,
            R.drawable.gift4, R.drawable.gift5, R.drawable.gift6,
            R.drawable.gift7, R.drawable.gift8, R.drawable.gift9,
            R.drawable.gift10, R.drawable.gift11, R.drawable.gift212,
            R.drawable.gift13, R.drawable.gift14, R.drawable.gift15,
            R.drawable.gift16, R.drawable.gift17, R.drawable.gift18,
            R.drawable.gift19, R.drawable.gift20
        )

        CoroutineScope(Dispatchers.IO).launch {
            for (imageResId in giftImages) {
                val giftImageEntity = GiftsImageEntity(imageResource = imageResId)
                giftImageDao.insertGiftImage(giftImageEntity)
            }
        }
    }

    private fun loadGiftImages() {
        CoroutineScope(Dispatchers.IO).launch {
            val giftImages = giftImageDao.getAllGiftImages()
            runOnUiThread {
                giftAdapter = GiftAdapter(giftImages.map { it.imageResource }) { imageId ->
                    saveGiftToHistory(imageId)
                }
                recyclerView.adapter = giftAdapter

                // Завершаем обновление при окончании загрузки данных
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun saveGiftToHistory(imageId: Long) {
        val gift = GiftsEntity(contactId = contactId, imageId = imageId)

        CoroutineScope(Dispatchers.IO).launch {
            giftDao.insertGift(gift)
            runOnUiThread {
                Toast.makeText(this@SelectGiftsActivity, "Подарок сохранен!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}