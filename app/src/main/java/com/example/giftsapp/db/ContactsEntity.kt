package com.example.giftsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
)

