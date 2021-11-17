package com.example.hearthstoneapp.data.network.repo

import com.example.hearthstone.data.network.endpoints.HearthStoneApiEndPoints
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstone.data.network.repoImpl.HearthStoneRepoImpl
import com.example.hearthstoneapp.data.network.endpoints.MapApiEndPoint
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.repoImpl.GoogleRepoImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface GoogleRepo {
    suspend fun fetchPlaces(
        location: String
    ): ServiceResult<MapsResponse?>

    companion object {
        fun provideGoogleRepo(
            dispatcher: Dispatchers, retroObjectMap: MapApiEndPoint
        ): GoogleRepo {
            return GoogleRepoImpl(dispatcher, retroObjectMap)
        }
    }
}
