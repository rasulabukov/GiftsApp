package com.example.giftsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)

