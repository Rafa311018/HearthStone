package com.example.hearthstoneapp.di.network

import com.example.hearthstone.data.network.endpoints.HearthStoneApiEndPoints
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstone.data.network.repoImpl.HearthStoneRepoImpl
import com.example.hearthstone.data.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HSNetworkModule {
    private const val HSBaseUrl = "https://omgvamp-hearthstone-v1.p.rapidapi.com/"

    @Singleton
    @Provides
    fun provideHSRetrofit(): HearthStoneApiEndPoints {
        return RetrofitFactory.retrofitProvider(
            HearthStoneApiEndPoints::class.java,
            HSBaseUrl
        )
    }

    @Singleton
    @Provides
    fun provideHSRepo(
        dispatcher: Dispatchers,
        retroObject: HearthStoneApiEndPoints
    ): HearthStoneRepo {
        return HearthStoneRepoImpl(dispatcher, retroObject)
    }
}
