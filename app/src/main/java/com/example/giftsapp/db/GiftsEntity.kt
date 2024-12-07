package com.example.giftsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifts")
data class GiftsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactId: Long,
    val imageId: Long
)
