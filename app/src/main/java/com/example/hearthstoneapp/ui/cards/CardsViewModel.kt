package com.example.hearthstoneapp.ui.cards

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hearthstoneapp.data.network.model.SearchResponse

class CardsViewModel(listCardR: SearchResponse/*, app Application*/) : ViewModel()  {

    private val _listCard = MutableLiveData<SearchResponse>()
    val listCard : LiveData<SearchResponse> = _listCard

    init {
        _listCard.value = listCardR
    }
}