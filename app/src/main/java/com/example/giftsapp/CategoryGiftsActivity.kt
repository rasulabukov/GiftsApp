package com.example.giftsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.giftsapp.db.GiftsImageEntity

class CategoryGiftsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var giftAdapter: CategoryAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_category)

        val back: ImageButton = findViewById(R.id.back)
        recyclerView = findViewById(R.id.cont_select_gifts)
        swipeRefreshLayout = findViewById(R.id.refresh)

        val contactId = intent.getLongExtra("CONTACT_ID", 0)

        back.setOnClickListener {
            finish()
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }

        val categories = listOf(
            GiftCategory("Мужчинам", R.drawable.men_category_image),
            GiftCategory("Женщинам", R.drawable.women_category_image),
            GiftCategory("Детям", R.drawable.kids_category_image),
            GiftCategory("Обувь", R.drawable.obuv),
            GiftCategory("Дом", R.drawable.dom),
            GiftCategory("Электроника", R.drawable.phone),
            GiftCategory("Цветы", R.drawable.flowers),
            GiftCategory("Спорт", R.drawable.sport),
        )

        giftAdapter = CategoryAdapter(categories) { category ->
            val intent = Intent(this, GiftsActivity::class.java)
            intent.putExtra("category_name", category.name)
            intent.putExtra("CONTACT_ID2", contactId)
            startActivity(intent)
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = giftAdapter
    }
}