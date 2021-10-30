package com.example.hearthstoneapp.ui.shops

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Shops"
    }
    val text: LiveData<String> = _text

}