package com.example.hearthstoneapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.data.database.FavoriteDatabaseDao
import com.example.hearthstoneapp.data.network.model.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val HSDao: FavoriteDatabaseDao
) : ViewModel() {

    private val _cardsFound = MutableLiveData<List<FavoriteCard>?>()
    val cardsFound: LiveData<List<FavoriteCard>?> = _cardsFound

    private val _cards = MutableLiveData<List<SearchResponse>>()
    val cards: LiveData<List<SearchResponse>> = _cards

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            _cardsFound.postValue(HSDao.getAllCards())
        }
    }

    fun updateCard(card: FavoriteCard): SearchResponse {
        val fCard = SearchResponse()
        fCard.cardId = card.cardId
        fCard.cardSet = card.cardSet
        fCard.effect = card.effect
        fCard.img = card.img
        fCard.type = card.type
        fCard.rarity = card.rarity
        fCard.playerClass = card.playerClass
        fCard.name = card.name
        return fCard
    }

    fun deleteFavorite(card: FavoriteCard) {
        viewModelScope.launch(Dispatchers.IO) {
            HSDao.delete(card.cardId)
        }
    }
}
