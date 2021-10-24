package com.example.hearthstoneapp.ui.cards.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearthstoneapp.data.network.model.SearchResponse
import com.example.hearthstoneapp.ui.mainscreen.adapter.HearthstoneViewHolder

class CardsAdapter: RecyclerView.Adapter<CardsViewHolder>() {
    private var hearthstoneCards: List<SearchResponse?>? = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.bind(hearthstoneCards?.get(position))
    }

    override fun getItemCount(): Int {
        return hearthstoneCards?.size ?: 0
    }
    fun setData(newCardsList: List<SearchResponse?>?) {
        this.hearthstoneCards = newCardsList
        notifyDataSetChanged()
    }
}