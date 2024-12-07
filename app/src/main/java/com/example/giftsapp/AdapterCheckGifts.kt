package com.example.giftsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.giftsapp.db.ContactsEntity
import com.example.giftsapp.db.GiftsEntity
import de.hdodenhof.circleimageview.CircleImageView

class AdapterCheckGifts(
    private val gifts: List<GiftsEntity>,
    private val contacts: List<ContactsEntity>,
    private val giftClickListener: (Long) -> Unit
) : RecyclerView.Adapter<AdapterCheckGifts.GiftViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gifts, parent, false)
        return GiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        val gift = gifts[position]
        val contact = contacts.find { it.id == gift.contactId }  // Ищем контакт по ID
        holder.bind(gift, contact)
    }

    override fun getItemCount(): Int = gifts.size

    inner class GiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contactNameTextView: TextView = view.findViewById(R.id.contactName)
        private val giftImageView: ImageView = view.findViewById(R.id.gift_image_story)
        private val contactImageView: CircleImageView = view.findViewById(R.id.ava)

        fun bind(gift: GiftsEntity, contact: ContactsEntity?) {
            contactNameTextView.text = contact?.name ?: "Контакт не найден"
            giftImageView.setImageResource(gift.imageId.toInt())
            contactImageView.setImageResource(R.drawable.default_ava)
        }
    }
}
