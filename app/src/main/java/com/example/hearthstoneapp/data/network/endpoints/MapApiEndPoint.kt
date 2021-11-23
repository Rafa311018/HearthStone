package com.example.hearthstoneapp.data.network.endpoints

import com.example.hearthstoneapp.BuildConfig
import com.example.hearthstoneapp.data.network.model.MapsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApiEndPoint {
    @GET("maps/api/place/nearbysearch/json?radius=100000")
    suspend fun fetchShops(
        @Query("type") type: String = "book_store",
        @Query("key") key: String = BuildConfig.GOOGLE_MAPS_API_KEY,
        @Query("location") location: String
    ): Response<MapsResponse?>
}
