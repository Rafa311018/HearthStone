package com.example.hearthstoneapp.di.network

import com.example.hearthstone.data.network.retrofit.RetrofitFactory
import com.example.hearthstoneapp.data.network.endpoints.MapApiEndPoint
import com.example.hearthstoneapp.data.network.repo.GoogleRepo
import com.example.hearthstoneapp.data.network.repoImpl.GoogleRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleNetworkModule {
    private  const val GoogleBaseUrl = "https://maps.googleapis.com/"

    @Singleton
    @Provides
    fun provideGoogleRetrofi(): MapApiEndPoint {
        return RetrofitFactory.retrofitProvider(
            MapApiEndPoint::class.java,
            GoogleBaseUrl
        )
    }

    @Singleton
    @Provides
    fun provideGoogleRepo(dispatcher: Dispatchers, retroObjectMap: MapApiEndPoint): GoogleRepo {
        return GoogleRepoImpl(dispatcher, retroObjectMap)
    }
}
