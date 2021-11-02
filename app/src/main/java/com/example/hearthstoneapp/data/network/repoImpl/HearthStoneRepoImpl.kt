package com.example.hearthstone.data.network.repoImpl

import android.util.Log
import com.example.hearthstone.data.network.model.HearthStoneResponse
import com.example.hearthstone.data.network.endpoints.HearthStoneApiEndPoints
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstone.data.network.retrofit.RetrofitFactory
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstoneapp.data.network.endpoints.MapApiEndPoint
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class HearthStoneRepoImpl() : HearthStoneRepo {
    val retroObject = RetrofitFactory.retrofitProvider(
        HearthStoneApiEndPoints::class.java,
        "https://omgvamp-hearthstone-v1.p.rapidapi.com/"
    )

    val retroObjectMap = RetrofitFactory.retrofitProvider(
        MapApiEndPoint::class.java,
        "https://maps.googleapis.com/"
    )

    override suspend fun fetchHearthStoneClasses(viewModelDispatcher: CoroutineDispatcher): ServiceResult<HearthStoneResponse?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObject.getInfo()

            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun searchCards(
        viewModelDispatcher: CoroutineDispatcher,
        cName: String
    ): ServiceResult<List<SearchResponse>?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObject.searchCards(name = cName)

            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun searchCardsbyClass(
        viewModelDispatcher: CoroutineDispatcher,
        cName: String
    ): ServiceResult<List<SearchResponse>?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObject.searchCardsbyClass(cardClass = cName)

            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun fetchPlaces(viewModelDispatcher: CoroutineDispatcher, latLng: String): ServiceResult<MapsResponse?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObjectMap.getShops(location = latLng)
            Log.d("Yoshi", "$dataResponse")
            if (dataResponse?.isSuccessful!!) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}