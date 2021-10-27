package com.example.hearthstoneapp.ui.cards

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.data.database.FavoriteDatabaseDao
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class CardsViewModel(val repo: HearthStoneRepo, val app: Application?, val database: FavoriteDatabaseDao) : ViewModel() {

    private val _cardsFound = MutableLiveData<List<SearchResponse>?>()
    val cardsFound: LiveData<List<SearchResponse>?> = _cardsFound

    private val _favoriteList = MutableLiveData<List<String>?>()
    val favoriteList: LiveData<List<String>?> = _favoriteList

    private val _showCards = MutableLiveData<Boolean>()
    val showCards: LiveData<Boolean> = _showCards

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    private val _searchCard = MutableLiveData<String?>()
    val searchCard: LiveData<String?> = _searchCard

    private val _startSearch = MutableLiveData<Boolean>()
    val startSearch: LiveData<Boolean> = _startSearch


    val dispatcher = Dispatchers.IO

    fun searchCards(card: String, search: String) {
        try {
            if (search == "card") {
                viewModelScope.launch(dispatcher) {
                    _searchString.postValue(app?.applicationContext?.getString(R.string.search_results, card))
                    when (val response = repo.searchCards(dispatcher, card)) {
                        is ServiceResult.Succes -> {
                            _cardsFound.postValue(response.data)
                            _showCards.postValue(true)
                        }
                        is ServiceResult.Error -> {
                            _showCards.postValue(false)
                            Timber.d("Error was found when calling Heartstone classes :: " + response.exception)
                        }
                        else -> {
                            Timber.d("Oh- oh... You've done fucked up...")
                        }
                    }

                }
            } else {
                viewModelScope.launch(dispatcher) {
                    _searchString.postValue(card)
                    when (val response = repo.searchCardsbyClass(dispatcher, card)) {
                        is ServiceResult.Succes -> {
                            _cardsFound.postValue(response.data)
                            _showCards.postValue(true)
                        }
                        is ServiceResult.Error -> {
                            _showCards.postValue(false)
                            Timber.d("Error was found when calling Hearthstone classes :: " + response.exception)
                        }
                        else -> {
                            Timber.d("Oh- oh... You've done fucked up...")
                        }
                    }

                }
                getFavorites()
            }
        } catch (e: Exception) {
            Timber.d(e)
        }

    }

    private fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteList.postValue(database.getAllIdCards())
        }
    }

    fun updateSearch(text: CharSequence) {
        _searchCard.value = text.toString()
    }

    fun searchButton() {
        if (null != _searchCard.value){
            _startSearch.value = true
            searchCards(requireNotNull(_searchCard.value),"card")
        }
        else{
            Toast.makeText(app?.applicationContext,"type what you want to search for", Toast.LENGTH_SHORT).show()
        }
    }

    fun doneSearch(){
        _startSearch.value = false
    }
}