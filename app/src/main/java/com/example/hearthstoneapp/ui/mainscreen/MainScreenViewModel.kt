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

    private val _search = MutableLiveData<String?>()
    val search: LiveData<String?> = _search

    private val _navitagionToCards = MutableLiveData<Boolean>()
    val navitagionToCards: LiveData<Boolean> = _navitagionToCards

    val dispatcher = Dispatchers.IO


    fun fetchClassList() {

        viewModelScope.launch(dispatcher) {
            when (val response = repo.fetchHearthStoneClasses(dispatcher)) {
                is ServiceResult.Succes -> {
                    _hearthStoneClasses.postValue(response.data?.classes)
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

    fun updateSearch(text: CharSequence) {
        _search.value = text.toString()
    }

    fun searchButton(){
        _navitagionToCards.value = true
    }

    fun doneNavigation(){
        _navitagionToCards.value = false
    }
}