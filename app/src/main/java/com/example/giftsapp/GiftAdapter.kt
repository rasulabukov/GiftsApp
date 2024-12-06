package com.example.giftsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giftsapp.db.GiftsEntity
class GiftAdapter(
    private val giftImages: List<Int>,
    private val onGiftClick: (Long) -> Unit // Функция для обработки клика по подарку
) : RecyclerView.Adapter<GiftAdapter.GiftViewHolder>() {

    inner class GiftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val giftImage: ImageView = itemView.findViewById(R.id.gift_image)

        fun bind(giftResId: Int) {
            Glide.with(giftImage.context)
                .load(giftResId)
                .into(giftImage)

            // Обработчик клика по изображению
            itemView.setOnClickListener {
                onGiftClick(giftResId.toLong()) // Передаем ID изображения в функцию
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_gifts, parent, false)
        return GiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        holder.bind(giftImages[position])
    }

    override fun getItemCount() = giftImages.size
}