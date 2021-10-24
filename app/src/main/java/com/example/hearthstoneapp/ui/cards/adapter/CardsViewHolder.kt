package com.example.hearthstoneapp.ui.cards.adapter

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.SearchResponse

class CardsViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.list_cards, parent, false
    )
) {

    val cardName = itemView.findViewById<TextView>(R.id.nameCard)
    val cardType = itemView.findViewById<TextView>(R.id.typeCard)
    val cardRarity = itemView.findViewById<TextView>(R.id.rarityCard)
    val cardSet = itemView.findViewById<TextView>(R.id.cardSet)
    val cardImage = itemView.findViewById<ImageView>(R.id.imageViewCard)

    fun bind(nameDataWrapper: SearchResponse?) {
        if (nameDataWrapper != null) {
            cardName.text = nameDataWrapper.name
            cardType.text = nameDataWrapper.type
            cardRarity.text = nameDataWrapper.rarity
            cardSet.text = nameDataWrapper.cardSet
        }
    }
}


/*class HearthstoneViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.grid_item_class, parent, false

         val type: String,
    val rarity: String,
    val cardSet: String,
    val img: String
    )
) {
    val className = itemView.findViewById<TextView>(R.id.nameClass)
    val classImage = itemView.findViewById<ImageView>(R.id.classImg)
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
}*/