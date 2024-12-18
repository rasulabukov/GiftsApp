package com.example.giftsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GiftImageDao {
    @Insert
    suspend fun insertGiftImage(giftImage: GiftsImageEntity)

    @Query("SELECT * FROM gifts_images")
    suspend fun getAllGiftImages(): List<GiftsImageEntity>

    @Query("SELECT * FROM gifts_images WHERE imageResource = :imageId LIMIT 1")
    suspend fun getGiftImageById(imageId: Int): GiftsImageEntity?
}