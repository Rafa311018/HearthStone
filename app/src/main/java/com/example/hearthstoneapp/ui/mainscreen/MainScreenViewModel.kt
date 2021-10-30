package com.example.hearthstoneapp.ui.mainscreen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainScreenViewModel(val app: Application, val repo: HearthStoneRepo) : ViewModel() {

    private val _hearthStoneClasses = MutableLiveData<List<String?>>()
    val hearthStoneClasses: LiveData<List<String?>> = _hearthStoneClasses

    private val _card = MutableLiveData<String?>()
    val card: LiveData<String?> = _card

    private val _navigateToCards = MutableLiveData<Boolean>()
    val navigateToCards: LiveData<Boolean> = _navigateToCards

    private val _showCards = MutableLiveData<Boolean>()
    val showCards: LiveData<Boolean> = _showCards

    val dispatcher = Dispatchers.IO


    fun fetchClassList() {
        viewModelScope.launch(dispatcher) {
            try {
                when (val response = repo.fetchHearthStoneClasses(dispatcher)) {
                    is ServiceResult.Succes -> {
                        _hearthStoneClasses.postValue(response.data?.classes)
                        _showCards.postValue(true)
                    }
                    is ServiceResult.Error -> {
                        Timber.d("Error was found when calling Heartstone classes :: ${response.exception}")
                        _showCards.postValue(false)
                    }
                    else -> {
                        Timber.d("Oh- oh... You've done fucked up...")
                    }
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    fun updateSearch(text: CharSequence) {
        _card.value = text.toString()
    }

    fun searchButton() {
        if (null != _card.value){
            _navigateToCards.value = true
        }
        else{
            Toast.makeText(app?.applicationContext,"type what you want to search for", Toast.LENGTH_SHORT).show()
        }
    }

    fun doneNavigation() {
        _navigateToCards.value = false
    }
}