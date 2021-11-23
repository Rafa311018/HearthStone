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
import com.example.hearthstoneapp.data.network.model.SearchResponse
import com.example.hearthstoneapp.databinding.ListCardsBinding

class FavoritesAdapter(
    private val clickListener: FavoritesListener
) : ListAdapter<FavoriteCard, RecyclerView.ViewHolder>(DiffCallBack()) {
    private var hearthstoneCards: List<FavoriteCard?>? = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                hearthstoneCards?.get(position)?.let { holder.bind(it, clickListener, position) }
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
        fun bind(item: FavoriteCard, clickListener: FavoritesListener, position: Int) {
            binding.favorite = item
            binding.imageViewFavoriteCard.setOnClickListener {
                clickListener.onClickCard(item, "details", position)
            }
            binding.likeIconF.setOnClickListener {
                clickListener.onClickCardF(item, "favorite", position)
            }
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

class DiffCallBack : DiffUtil.ItemCallback<FavoriteCard>() {
    override fun areItemsTheSame(oldItem: FavoriteCard, newItem: FavoriteCard): Boolean {
        return oldItem.cardId == newItem.cardId
    }

    override fun areContentsTheSame(oldItem: FavoriteCard, newItem: FavoriteCard): Boolean {
        return oldItem == newItem
    }
}

class FavoritesListener(
    val clickListener:
        (card: FavoriteCard, click: String, position: Int) -> Unit
) {
    fun onClickCard(card: FavoriteCard, identifier: String, position: Int) =
        clickListener(card, identifier, position)

    fun onClickCardF(card: FavoriteCard, identifier: String, position: Int) =
        clickListener(card, identifier, position)
}
