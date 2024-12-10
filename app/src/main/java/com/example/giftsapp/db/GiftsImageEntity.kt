package com.example.giftsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifts_images")
data class GiftsImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imageResource: Int,
    val name: String
)