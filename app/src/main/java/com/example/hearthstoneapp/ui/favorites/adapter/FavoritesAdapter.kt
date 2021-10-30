package com.example.hearthstoneapp.ui.favorites.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.databinding.ListCardsBinding

class FavoritesAdapter(val clickListener: FavoritesListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallBack()) {
    private var hearthstoneCards: List<FavoriteCard?>? = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                hearthstoneCards?.get(position)?.let { holder.bind(it, clickListener) }
            }
        }
    }

    override fun getItemCount(): Int {
        return hearthstoneCards?.size ?: 0
    }

    fun setData(newCardsList: List<FavoriteCard?>?) {
        this.hearthstoneCards = newCardsList
        notifyDataSetChanged()
    }

    class ViewHolder private constructor(val binding: ListCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteCard, clickListener: FavoritesListener) {
            binding.favorite = item
            binding.favoriteListener = clickListener
            binding.likeIconF.setImageResource(R.drawable.basic_heart_fill)
            binding.likeIconF.visibility = View.VISIBLE
            binding.likeIcon.visibility = View.GONE
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListCardsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class CardItem(val card: FavoriteCard) : DataItem() {
        override val id = card.cardId
    }

    abstract val id: String
}

class FavoritesListener(val clickListener: (card: FavoriteCard, click: String) -> Unit) {
    fun onClickCard(card: FavoriteCard) = clickListener(card, "details")
    fun onClickCardF(card: FavoriteCard) = clickListener(card,"favorite")
}