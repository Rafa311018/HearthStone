package com.example.hearthstoneapp.ui.carddetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.data.database.FavoriteDatabaseDao
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardDetailsViewModel(
    val database: FavoriteDatabaseDao,
    application: Application
) : ViewModel(
) {
    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> = _favorite

    var idCard = String()

    private var fCard = MutableLiveData<FavoriteCard>()

    private var _favoriteCard = MutableLiveData<FavoriteCard?>()
    val favoriteCard: LiveData<FavoriteCard?> = _favoriteCard


    fun isFavorite() {
        viewModelScope.launch {
            _favoriteCard.postValue(getFavoriteCardFromDatabase())
        }
    }

    fun favoriteHeart() {
        if (_favoriteCard.value?.cardId != " "){
            _favorite.postValue(true)
        }else{
            _favorite.postValue(false)
        }
    }

    fun clickOnFavorite(card: SearchResponse){
        if (_favorite.value == true){
            viewModelScope.launch {
                deleteFromDatabase()
                _favorite.postValue(false)
            }
        }else{
            val newFavoriteCard = FavoriteCard()
            newFavoriteCard.cardId = card.cardId.toString()
            newFavoriteCard.cardSet = card.cardSet
            newFavoriteCard.effect = card.effect
            newFavoriteCard.img = card.img
            newFavoriteCard.name = card.name.toString()
            newFavoriteCard.playerClass = card.playerClass
            newFavoriteCard.rarity = card.rarity
            newFavoriteCard.type = card.type
            _favorite.postValue(true)
            viewModelScope.launch {
                addFavorite(newFavoriteCard)
            }
        }
    }

    private suspend fun addFavorite(card: FavoriteCard) {
        withContext(Dispatchers.IO) {
            database.insert(card)
        }
    }

    private suspend fun deleteFromDatabase() {
        withContext(Dispatchers.IO) {
            database.delete(idCard)
        }
    }

    private suspend fun getFavoriteCardFromDatabase(): FavoriteCard? {
        return withContext(Dispatchers.IO){
            var card = database.getCard(idCard)
            card
        }
    }
}