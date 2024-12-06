package com.example.giftsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, ContactsFragment())
                .commit()
        }

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.contacts -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, ContactsFragment())
                        .commit()
                    true
                }
                R.id.gifts -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, GiftsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
}
}
