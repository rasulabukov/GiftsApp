package com.example.giftsapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
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

class GiftsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var giftAdapter: GiftAdapter
    private lateinit var giftImageDao: GiftImageDao
    private lateinit var giftDao: GiftsDao
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var contactId: Long = 0
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gifts)

        val back: ImageButton = findViewById(R.id.back)
        val category_name: TextView = findViewById(R.id.category_name)
        giftImageDao = AppDatabase.getDatabase(this).giftImageDao()
        giftDao = AppDatabase.getDatabase(this).giftDao()

        swipeRefreshLayout = findViewById(R.id.refresh)
        recyclerView = findViewById(R.id.cont_select_gifts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        contactId = intent.getLongExtra("CONTACT_ID2", 0)
        categoryName = intent.getStringExtra("category_name")
        category_name.text = categoryName

        Log.d("Activity", "Sending gift for contact with ID: $contactId")
        Log.d("Activity", "Category: $categoryName")

        saveGiftImages()
        loadGiftImages()

        back.setOnClickListener {
            finish()
        }

        swipeRefreshLayout.setOnRefreshListener {
            loadGiftImages()
        }
    }

    private fun saveGiftImages() {
        val giftImages = listOf(
            // Мужчинам
            R.drawable.gift1, R.drawable.gift2, R.drawable.gift3, R.drawable.gift4, R.drawable.gift5,
            R.drawable.gift6, R.drawable.gift7, R.drawable.gift8, R.drawable.gift9, R.drawable.gift10,
            // Женщинам
            R.drawable.gift11, R.drawable.gift12, R.drawable.gift13, R.drawable.gift14, R.drawable.gift15,
            R.drawable.gift16, R.drawable.gift17, R.drawable.gift18, R.drawable.gift19, R.drawable.gift20,
            // Детям
            R.drawable.gift21, R.drawable.gift22, R.drawable.gift23, R.drawable.gift24, R.drawable.gift25,
            R.drawable.gift26, R.drawable.gift27, R.drawable.gift28, R.drawable.gift29, R.drawable.gift30,
            // Обувь
            R.drawable.gift31, R.drawable.gift32, R.drawable.gift33, R.drawable.gift34, R.drawable.gift35,
            R.drawable.gift36, R.drawable.gift37, R.drawable.gift38, R.drawable.gift39, R.drawable.gift40,
            // Дом
            R.drawable.gift41, R.drawable.gift42, R.drawable.gift43, R.drawable.gift44, R.drawable.gift45,
            R.drawable.gift46, R.drawable.gift47, R.drawable.gift48, R.drawable.gift49, R.drawable.gift50,
            // Электроника
            R.drawable.gift51, R.drawable.gift52, R.drawable.gift53, R.drawable.gift54, R.drawable.gift55,
            R.drawable.gift56, R.drawable.gift57, R.drawable.gift58, R.drawable.gift59, R.drawable.gift60,
            // Цветы
            R.drawable.gift61, R.drawable.gift62, R.drawable.gift63, R.drawable.gift64, R.drawable.gift65,
            R.drawable.gift66, R.drawable.gift67, R.drawable.gift68, R.drawable.gift69, R.drawable.gift70,
            // Спорт
            R.drawable.gift71, R.drawable.gift72, R.drawable.gift73, R.drawable.gift74, R.drawable.gift75,
            R.drawable.gift76, R.drawable.gift77, R.drawable.gift78, R.drawable.gift79, R.drawable.gift80
        )

        val giftNames = listOf(
            // Мужчинам
            "Часы мужские", "Ремень кожаный", "Набор для бритья", "Портмоне", "Очки солнцезащитные",
            "Галстук", "Зажигалка", "Портфель", "Бритва электрическая", "Кошелек",
            // Женщинам
            "Сумка женская", "Серьги", "Кольцо", "Шарф", "Туфли",
            "Платье", "Клатч", "Колье", "Браслет", "Помада",
            // Детям
            "Игрушка детская", "Конструктор", "Кукла", "Мяч", "Пазл",
            "Книжка-раскраска", "Набор для творчества", "Машинка", "Кубик Рубика", "Настольная игра",
            // Обувь
            "Кроссовки", "Кеды", "Ботинки", "Сандалии", "Тапочки",
            "Лоферы", "Сапоги", "Сланцы", "Туфли офисные", "Кроссовки для бега",
            // Дом
            "Настольная лампа", "Настольный вентилятор", "Настольные часы", "Настольный органайзер", "Настольная игра",
            "Настольный календарь", "Настольная лампа с USB", "Настольная лампа с часами", "Настольный вентилятор с подсветкой", "Настольная лампа сенсорная",
            // Электроника
            "Наушники", "Смартфон", "Планшет", "Умные часы", "Фитнес-браслет",
            "Портативная колонка", "Внешний аккумулятор", "Электронная книга", "Беспроводная зарядка", "Камера мгновенной печати",
            // Цветы
            "Букет роз", "Букет тюльпанов", "Букет лилий", "Букет хризантем", "Букет орхидей",
            "Букет пионов", "Букет гербер", "Букет ромашек", "Букет гвоздик", "Букет альстромерий",
            // Спорт
            "Мяч футбольный", "Гантели", "Скакалка", "Коврик для йоги", "Велосипед",
            "Роликовые коньки", "Тренажер для пресса", "Боксерская груша", "Теннисная ракетка", "Беговая дорожка"
        )

        val giftDesc = listOf(
            // Мужчинам
            "Элегантные часы для мужчин", "Кожаный ремень для мужчин", "Набор для бритья", "Стильное портмоне", "Солнцезащитные очки",
            "Классический галстук", "Стильная зажигалка", "Кожаный портфель", "Электрическая бритва", "Кожаный кошелек",
            // Женщинам
            "Модная сумка для женщин", "Элегантные серьги", "Красивое кольцо", "Модный шарф", "Красивые туфли",
            "Элегантное платье", "Стильный клатч", "Красивое колье", "Модный браслет", "Качественная помада",
            // Детям
            "Мягкая игрушка для детей", "Развивающий конструктор", "Мягкая кукла", "Яркий мяч", "Интересный пазл",
            "Развивающая книжка-раскраска", "Набор для творчества", "Игрушечная машинка", "Головоломка Кубик Рубика", "Увлекательная настольная игра",
            // Обувь
            "Стильные кроссовки", "Удобные кеды", "Теплые ботинки", "Легкие сандалии", "Уютные тапочки",
            "Классические лоферы", "Зимние сапоги", "Летние сланцы", "Офисные туфли", "Кроссовки для бега",
            // Дом
            "Современная настольная лампа", "Компактный настольный вентилятор", "Элегантные настольные часы", "Практичный настольный органайзер", "Увлекательная настольная игра",
            "Стильный настольный календарь", "Настольная лампа с USB-портом", "Настольная лампа с часами", "Настольный вентилятор с подсветкой", "Сенсорная настольная лампа",
            // Электроника
            "Качественные наушники", "Современный смартфон", "Многофункциональный планшет", "Умные часы с трекером", "Фитнес-браслет с пульсометром",
            "Портативная колонка с Bluetooth", "Внешний аккумулятор большой емкости", "Электронная книга с подсветкой", "Беспроводная зарядка для смартфона", "Камера мгновенной печати",
            // Цветы
            "Красивый букет роз", "Яркий букет тюльпанов", "Элегантный букет лилий", "Нежный букет хризантем", "Экзотический букет орхидей",
            "Романтический букет пионов", "Яркий букет гербер", "Простой букет ромашек", "Классический букет гвоздик", "Необычный букет альстромерий",
            // Спорт
            "Качественный футбольный мяч", "Набор гантелей", "Прочная скакалка", "Коврик для йоги", "Современный велосипед",
            "Роликовые коньки для взрослых", "Тренажер для пресса", "Боксерская груша", "Теннисная ракетка", "Беговая дорожка"
        )

        val giftCategory = listOf(
            // Мужчинам
            "Мужчинам", "Мужчинам", "Мужчинам", "Мужчинам", "Мужчинам",
            "Мужчинам", "Мужчинам", "Мужчинам", "Мужчинам", "Мужчинам",
            // Женщинам
            "Женщинам", "Женщинам", "Женщинам", "Женщинам", "Женщинам",
            "Женщинам", "Женщинам", "Женщинам", "Женщинам", "Женщинам",
            // Детям
            "Детям", "Детям", "Детям", "Детям", "Детям",
            "Детям", "Детям", "Детям", "Детям", "Детям",
            // Обувь
            "Обувь", "Обувь", "Обувь", "Обувь", "Обувь",
            "Обувь", "Обувь", "Обувь", "Обувь", "Обувь",
            // Дом
            "Дом", "Дом", "Дом", "Дом", "Дом",
            "Дом", "Дом", "Дом", "Дом", "Дом",
            // Электроника
            "Электроника", "Электроника", "Электроника", "Электроника", "Электроника",
            "Электроника", "Электроника", "Электроника", "Электроника", "Электроника",
            // Цветы
            "Цветы", "Цветы", "Цветы", "Цветы", "Цветы",
            "Цветы", "Цветы", "Цветы", "Цветы", "Цветы",
            // Спорт
            "Спорт", "Спорт", "Спорт", "Спорт", "Спорт",
            "Спорт", "Спорт", "Спорт", "Спорт", "Спорт"
        )

        CoroutineScope(Dispatchers.IO).launch {
            for (i in giftImages.indices) {
                val giftImageEntity = GiftsImageEntity(
                    imageResource = giftImages[i],
                    name = giftNames[i],
                    description = giftDesc[i],
                    category = giftCategory[i]
                )

                // Проверяем, существует ли уже такой подарок в базе данных
                val existingGift = giftImageDao.getGiftImageByResource(giftImages[i])
                if (existingGift == null) {
                    // Если подарка нет, добавляем его
                    giftImageDao.insertGiftImage(giftImageEntity)
                }
            }
        }
    }

    private fun loadGiftImages() {
        CoroutineScope(Dispatchers.IO).launch {
            val giftImages = if (categoryName != null) {
                giftImageDao.getGiftImagesByCategory(categoryName!!)
            } else {
                giftImageDao.getAllGiftImages()
            }

            runOnUiThread {
                giftAdapter = GiftAdapter(giftImages) { imageId ->
                    saveGiftToHistory(imageId)
                }
                recyclerView.adapter = giftAdapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun saveGiftToHistory(imageId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val giftImageEntity = giftImageDao.getGiftImageById(imageId.toInt())

            giftImageEntity?.let { giftEntity ->
                val gift = GiftsEntity(
                    contactId = contactId,
                    imageId = giftEntity.imageResource.toLong(),
                    name = giftEntity.name,
                )

                giftDao.insertGift(gift)
                runOnUiThread {
                    Toast.makeText(this@GiftsActivity, "Подарок сохранен!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } ?: run {
                runOnUiThread {
                    Toast.makeText(this@GiftsActivity, "Ошибка: подарок не найден.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}