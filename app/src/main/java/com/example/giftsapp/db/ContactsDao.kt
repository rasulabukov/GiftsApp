package com.example.giftsapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactsDao {
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<ContactsEntity>

    @Insert
    fun insert(contact: ContactsEntity): Long

    @Update
    fun updateContact(contact: ContactsEntity)

    @Delete
    fun delete(contact: ContactsEntity)

}