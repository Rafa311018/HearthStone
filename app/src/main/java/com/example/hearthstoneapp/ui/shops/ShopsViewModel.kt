package com.example.hearthstoneapp.ui.shops

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.repo.GoogleRepo
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val GoogleRepo: GoogleRepo,
    private val dispatcher: Dispatchers
) : ViewModel() {

    private val _searchPlaces = MutableLiveData<MapsResponse?>()
    val searchPlaces: LiveData<MapsResponse?> = _searchPlaces

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    val _latLng = MutableLiveData<LatLng>()
    val latLng: LiveData<LatLng> = _latLng


    fun searchPlaces(location: String) {
        viewModelScope.launch(dispatcher.IO) {
            when (val response = GoogleRepo.fetchPlaces(
                location
            )) {
                is ServiceResult.Succes -> {
                    _searchPlaces.postValue(response.data)
                    Timber.d(response.toString())
                }
                is ServiceResult.Error -> {
                    Timber.d(
                        "Error was found when calling maps :: "
                                + response.exception
                    )
                }
                else -> {
                    Timber.d("Oh- oh... Something is wrong...")
                }
            }

        }
    }
}
