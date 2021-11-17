package com.example.hearthstoneapp.data.network.repoImpl

import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstoneapp.data.network.endpoints.MapApiEndPoint
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.repo.GoogleRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleRepoImpl @Inject constructor(
    private val dispatcher: Dispatchers,
    private val retroObjectMap: MapApiEndPoint
) : GoogleRepo {

    override suspend fun fetchPlaces(
        latLng: String
    ): ServiceResult<MapsResponse?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObjectMap.getShops(location = latLng)
            if (dataResponse?.isSuccessful!!) {
                ServiceResult.Succes(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}
