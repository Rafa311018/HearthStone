package com.example.hearthstoneapp.ui.favorites

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.data.database.FavoriteDatabaseDao
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class FavoritesViewModel(
    val database: FavoriteDatabaseDao
) : ViewModel() {

    private val _cardsFound = MutableLiveData<List<FavoriteCard>?>()
    val cardsFound: LiveData<List<FavoriteCard>?> = _cardsFound

    private val _cards = MutableLiveData<List<SearchResponse>>()
    val cards: LiveData<List<SearchResponse>> = _cards

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            _cardsFound.postValue(database.getAllCards())
        }
    }

    fun updateCard(card: FavoriteCard): SearchResponse {
        val fcard = SearchResponse()
        fcard.cardId = card.cardId
        fcard.cardSet = card.cardSet
        fcard.effect = card.effect
        fcard.img = card.img
        fcard.type = card.type
        fcard.rarity = card.rarity
        fcard.playerClass = card.playerClass
        fcard.name = card.name
        return fcard
    }
}