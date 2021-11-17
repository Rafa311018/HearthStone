package com.example.hearthstone.data.network.repo

import com.example.hearthstone.data.network.endpoints.HearthStoneApiEndPoints
import com.example.hearthstone.data.network.model.HearthStoneResponse
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstone.data.network.repoImpl.HearthStoneRepoImpl
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface HearthStoneRepo {

    suspend fun fetchHearthStoneClasses(): ServiceResult<HearthStoneResponse?>

    suspend fun searchCards(
        cName: String
    ): ServiceResult<List<SearchResponse>?>

    suspend fun searchCardsByClass(
        cName: String
    ): ServiceResult<List<SearchResponse>?>

    companion object {
        fun provideHearthStoneRepo(
            dispatcher: Dispatchers, retroObject: HearthStoneApiEndPoints
        ): HearthStoneRepo {
            return HearthStoneRepoImpl(dispatcher, retroObject)
        }
    }
}
