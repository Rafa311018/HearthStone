package com.example.hearthstone.data.network.repoImpl

import com.example.hearthstone.data.network.endpoints.HearthStoneApiEndPoints
import com.example.hearthstone.data.network.model.HearthStoneResponse
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstone.data.network.retrofit.RetrofitFactory
import com.example.hearthstoneapp.data.network.endpoints.MapApiEndPoint
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HearthStoneRepoImpl @Inject constructor(
    private val dispatcher: Dispatchers,
    private val retroObject: HearthStoneApiEndPoints
) : HearthStoneRepo {

    val retroObjectMap = RetrofitFactory.retrofitProvider(
        MapApiEndPoint::class.java,
        "https://maps.googleapis.com/"
    )

    override suspend fun fetchHearthStoneClasses(
    ): ServiceResult<HearthStoneResponse?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.getInfo()
            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun searchCards(
        cName: String
    ): ServiceResult<List<SearchResponse>?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.searchCards(name = cName)
            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun searchCardsByClass(
        cName: String
    ): ServiceResult<List<SearchResponse>?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.searchCardsbyClass(cardClass = cName)

            if (dataResponse.isSuccessful) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}
