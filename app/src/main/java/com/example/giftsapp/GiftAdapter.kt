package com.example.giftsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giftsapp.db.GiftsImageEntity

class GiftAdapter(
    private val giftImages: List<GiftsImageEntity>,
    private val onGiftClick: (Long) -> Unit
) : RecyclerView.Adapter<GiftAdapter.GiftViewHolder>() {

    inner class GiftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val giftImage: ImageView = itemView.findViewById(R.id.gift_image)
        val giftName: TextView = itemView.findViewById(R.id.giftName)
        val giftDesc: TextView = itemView.findViewById(R.id.giftDesc)

        fun bind(gift: GiftsImageEntity) {
            Glide.with(giftImage.context)
                .load(gift.imageResource)
                .into(giftImage)
            giftName.text = gift.name
            giftDesc.text = gift.description

            itemView.setOnClickListener {
                onGiftClick(gift.imageResource.toLong())
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