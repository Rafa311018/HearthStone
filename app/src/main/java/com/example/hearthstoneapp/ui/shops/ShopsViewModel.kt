package com.example.hearthstoneapp.ui.shops

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ShopsViewModel(val repo: HearthStoneRepo, val app: Application?) : ViewModel() {

    private val _searchPlaces = MutableLiveData<MapsResponse?>()
    val searchPlaces: LiveData<MapsResponse?> = _searchPlaces

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    val _latLng = MutableLiveData<LatLng>()
    val latLng: LiveData<LatLng> = _latLng

    private val dispatcher = Dispatchers.IO


    fun searchPlaces(latLng: LatLng) {
        viewModelScope.launch(dispatcher) {
            Log.d("Yoshi", "l $latLng")
//            val lat = (latLng.latitude.toString() + "," + latLng.longitude.toString())
            when (val response = repo.fetchPlaces(
                dispatcher,
                (latLng.latitude.toString() + "," + latLng.longitude.toString())
            )) {
                is ServiceResult.Succes -> {
                    _searchPlaces.postValue(response.data)
                    Log.d("Yoshi", "$response")
                }
                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling Heartstone classes :: " + response.exception)
                    Log.d("Yoshi", "$response")
                }
                else -> {
                    Timber.d("Oh- oh... You've done fucked up...")
                    Log.d("Yoshi", "$response")
                }
            }

        }
    }
}