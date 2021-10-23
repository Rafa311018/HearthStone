package com.example.hearthstoneapp.ui.mainscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ClassesAdapter() : RecyclerView.Adapter<HearthstoneViewHolder>() {
    private var hearthstoneClasses: List<String?>? = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HearthstoneViewHolder {
        return HearthstoneViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: HearthstoneViewHolder, position: Int) {
        holder.bind(hearthstoneClasses?.get(position))
    }

    override fun getItemCount(): Int {
        return hearthstoneClasses?.size ?: 0
    }

    fun setData(newClassesList: List<String?>?) {
        this.hearthstoneClasses = newClassesList
        notifyDataSetChanged()
    }
}