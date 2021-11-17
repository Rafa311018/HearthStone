package com.example.hearthstoneapp.ui.mainscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hearthstoneapp.R

class HearthstoneViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.grid_item_class, parent, false
    )
) {
    val className = itemView.findViewById<TextView>(R.id.nameClass)
    val classImage: ImageView = itemView.findViewById<ImageView>(R.id.classImg)

    fun bind(classDataWrapper: String?) {
        className.text = classDataWrapper
        classImage.setImageResource(
            when (classDataWrapper) {
                "Death Knight" -> R.drawable.no_image
                "Druid" -> R.drawable.druid
                "Hunter" -> R.drawable.hunter
                "Mage" -> R.drawable.mage
                "Paladin" -> R.drawable.paladin
                "Priest" -> R.drawable.priest
                "Rogue" -> R.drawable.rogue
                "Shaman" -> R.drawable.shaman
                "Warlock" -> R.drawable.warlock
                "Warrior" -> R.drawable.warrior
                "Dream" -> R.drawable.no_image
                "Neutral" -> R.drawable.no_image
                "Whizbang" -> R.drawable.no_image
                "Demon Hunter" -> R.drawable.demon_hunter
                else -> R.drawable.no_image
            }
        )
    }
}
