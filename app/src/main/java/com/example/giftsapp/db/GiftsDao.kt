package com.example.giftsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GiftsDao {
    @Insert
    fun insertGift(gift: GiftsEntity)

    @Query("SELECT * FROM gifts") // Получаем все подарки
    suspend fun getAllGifts(): List<GiftsEntity>

    @Query("SELECT * FROM gifts WHERE contactId = :contactId")
    suspend fun getGiftsContact(contactId: Int): List<GiftsEntity>
}
