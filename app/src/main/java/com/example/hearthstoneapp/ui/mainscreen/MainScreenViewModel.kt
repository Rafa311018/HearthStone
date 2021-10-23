package com.example.hearthstoneapp.ui.mainscreen

import android.app.Application
import android.util.Log
import android.widget.Toast
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

class MainScreenViewModel(val app: Application, val repo: HearthStoneRepo) : ViewModel() {

    private val _hearthStoneClasses = MutableLiveData<List<String?>>()
    val hearthStoneClasses: LiveData<List<String?>> = _hearthStoneClasses
    val dispatcher = Dispatchers.IO

    private val _cardsFound = MutableLiveData<List<SearchResponse>?>()
    val cardsFound: LiveData<List<SearchResponse>?> = _cardsFound

    private val _search = MutableLiveData<String>()
    val search: LiveData<String> = _search


    fun fetchClassList() {

        viewModelScope.launch(dispatcher) {
            when (val response = repo.fetchHearthStoneClasses(dispatcher)) {
                is ServiceResult.Succes -> {
                    _hearthStoneClasses.postValue(response.data?.classes)
                    Timber.d("Oh- oh... You've done fucked up...")
                }
                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling Heartstone classes :: ${response.exception}")
                }
                else -> {
                    Timber.d("Oh- oh... You've done fucked up...")
                }
            }
        }
    }

    fun searchCards() {
        if (_search.value != null) {
            viewModelScope.launch(dispatcher) {

                when (val response = _search.value?.let { repo.searchCards(dispatcher, it) }) {
                    is ServiceResult.Succes -> {
                        _cardsFound.postValue(response.data)
                    }
                    is ServiceResult.Error -> {
                        Timber.d("Error was found when calling Heartstone classes :: ${response.exception}")
                    }
                    else -> {
                        Timber.d("Oh- oh... You've done fucked up...")
                    }
                }

            }
        } else {
            Toast.makeText(
                app.applicationContext,
                "Please enter what you want to search for",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateSearch(text: CharSequence) {
        Log.d("Yoshi","$text")
        _search.value = text.toString()
    }
}