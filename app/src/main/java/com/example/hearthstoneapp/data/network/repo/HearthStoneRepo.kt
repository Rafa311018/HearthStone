package com.example.hearthstone.data.network.repo

import com.example.hearthstone.data.network.model.HearthStoneResponse
import com.example.hearthstone.data.network.repoImpl.HearthStoneRepoImpl
import com.example.hearthstone.data.network.model.networkmodel.ServiceResult
import com.example.hearthstoneapp.data.network.model.SearchResponse
import kotlinx.coroutines.CoroutineDispatcher

interface HearthStoneRepo {
    suspend fun fetchHearthStoneClasses(viewModelDispatcher: CoroutineDispatcher): ServiceResult<HearthStoneResponse?>
    suspend fun searchCards(viewModelDispatcher: CoroutineDispatcher, cName: String): ServiceResult<List<SearchResponse>?>

    companion object{
        fun provideHearthStoneRepo(): HearthStoneRepo{
            return  HearthStoneRepoImpl()
        }
    }
}