package com.example.hearthstoneapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.SearchResponse
import com.example.hearthstoneapp.databinding.ListCardsBinding

class CardsAdapter(private val clickListener: CardListener) :
    ListAdapter<SearchResponse, RecyclerView.ViewHolder>(DiffCallBack()) {
    private var hearthstoneCards: List<SearchResponse?>? = listOf()
    private var favoriteList: List<String>? = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                hearthstoneCards?.get(position)?.let {
                    holder.bind(it, clickListener, favoriteList, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return hearthstoneCards?.size ?: 0
    }

    fun setData(newCardsList: List<SearchResponse?>?, favoriteList: List<String>?) {
        this.hearthstoneCards = newCardsList
        this.favoriteList = favoriteList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ListCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: SearchResponse,
            clickListener: CardListener,
            favoriteList: List<String>?,
            position: Int
        ) {
            binding.card = item
            binding.imageViewCard.setOnClickListener {
                clickListener.onClickCard(item, "details", position)
            }
            binding.likeIcon.setOnClickListener {
                clickListener.onClickCardF(item, "favorite", position)
            }
            if (favoriteList != null) {
                if (favoriteList.contains(item.cardId))
                    binding.likeIcon.setImageResource(R.drawable.basic_heart_fill)
                else binding.likeIcon.setImageResource(R.drawable.basic_heart_outline)
            }
            binding.likeIconF.visibility = View.GONE
            binding.likeIcon.visibility = View.VISIBLE
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

class DiffCallBack : DiffUtil.ItemCallback<SearchResponse>() {
    override fun areItemsTheSame(oldItem: SearchResponse, newItem: SearchResponse): Boolean {
        return oldItem.cardId == newItem.cardId
    }

    override fun areContentsTheSame(oldItem: SearchResponse, newItem: SearchResponse): Boolean {
        return oldItem == newItem
    }
}

class CardListener(
    val clickListener:
        (card: SearchResponse, click: String, position: Int) -> Unit
) {
    fun onClickCard(card: SearchResponse, identifier: String, position: Int) =
        clickListener(card, identifier, position)

    fun onClickCardF(card: SearchResponse, identifier: String, position: Int) =
        clickListener(card, identifier, position)
}
