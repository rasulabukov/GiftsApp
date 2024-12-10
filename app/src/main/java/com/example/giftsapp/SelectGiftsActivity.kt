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
    private var contactId: Long = 0

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

        contactId = intent.getLongExtra("CONTACT_ID", 0)
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
            R.drawable.gift10, R.drawable.gift11, R.drawable.gift12,
            R.drawable.gift13, R.drawable.gift14, R.drawable.gift15,
            R.drawable.gift16, R.drawable.gift17, R.drawable.gift18,
            R.drawable.gift19, R.drawable.gift20, R.drawable.gift21
            , R.drawable.gift22, R.drawable.gift23, R.drawable.gift24
            , R.drawable.gift25, R.drawable.gift26, R.drawable.gift27
            , R.drawable.gift28, R.drawable.gift29
        )

        val giftNames = listOf(
            "Новогодний набор 1", "Новогодний подарок", "Шоколадка Мишка",
            "Iphone 16 pro max", "Xiaomi redmi 14c", "Techno",
            "Смартфон", "Мини телефон i16 pro mini", "Студенческая камера",
            "Мини камера нового поколения", "Фотоаппарат Fujifilm", "Цветы Альстромерии",
            "Цветы роза", "Цветы", "Сенсорный ночник",
            "Большой фонарь", "RGB светильник", "Ночник с эффектом сияния",
            "Настольная лампа", "Настенный беспроводной светильник", "Ночник букет",
            "Проектор Ночник", "Планшет Lingbo", "Планшет Atouch",
            "Планшет", "Планшет с клавиатурой и чехлом", "Ipad",
            "Беспроводная игровая мышь", "Рюкзак трансформер"
        )

        CoroutineScope(Dispatchers.IO).launch {
            for (i in giftImages.indices) {
                val giftImageEntity = GiftsImageEntity(imageResource = giftImages[i], name = giftNames[i])
                giftImageDao.insertGiftImage(giftImageEntity)
            }
        }
    }

    private fun loadGiftImages() {
        CoroutineScope(Dispatchers.IO).launch {
            val giftImages = giftImageDao.getAllGiftImages()
            runOnUiThread {
                giftAdapter = GiftAdapter(giftImages) { imageId ->
                    saveGiftToHistory(imageId)
                }
                recyclerView.adapter = giftAdapter

                // Завершаем обновление при окончании загрузки данных
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun saveGiftToHistory(imageId: Long) {
        // Приводим imageId к Int, если у вас сохранен как Int
        CoroutineScope(Dispatchers.IO).launch {
            // Получаем объект подарка по imageId
            val giftImageEntity = giftImageDao.getGiftImageById(imageId.toInt())

            // Если подарок найден, создаем подарочную запись
            giftImageEntity?.let { giftEntity ->
                val gift = GiftsEntity(
                    contactId = contactId,
                    imageId = giftEntity.imageResource.toLong(),
                    name = giftEntity.name // Получаем имя подарка
                )

                giftDao.insertGift(gift)
                runOnUiThread {
                    Toast.makeText(this@SelectGiftsActivity, "Подарок сохранен!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } ?: run {
                // Если подарок не найден, выводим сообщение об ошибке
                runOnUiThread {
                    Toast.makeText(this@SelectGiftsActivity, "Ошибка: подарок не найден.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}