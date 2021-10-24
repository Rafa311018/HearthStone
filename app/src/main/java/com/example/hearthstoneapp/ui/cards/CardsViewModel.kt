package com.example.hearthstoneapp.ui.cards

import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class CardsViewModel(val repo: HearthStoneRepo /*, app Application*/) : ViewModel() {

    private val _cardsFound = MutableLiveData<List<SearchResponse>?>()
    val cardsFound: LiveData<List<SearchResponse>?> = _cardsFound


    val dispatcher = Dispatchers.IO

    fun searchCards(search: String) {
        viewModelScope.launch(dispatcher) {

            when (val response = repo.searchCards(dispatcher, search)) {
                is ServiceResult.Succes -> {
                    _cardsFound.postValue(response.data)
                }
                is ServiceResult.Error -> {
                    Log.d("Yoshi","Error was found when calling Heartstone classes :: ${response.exception}")
                }
                else -> {
                    Log.d("Yoshi","Oh- oh... You've done fucked up...")
                }
            }

        }
    }
}