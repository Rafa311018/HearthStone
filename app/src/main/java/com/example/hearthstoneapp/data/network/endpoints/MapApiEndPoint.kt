package com.example.hearthstoneapp.data.network.endpoints

import com.example.hearthstoneapp.BuildConfig.GOOGLE_MAPS_API_KEY
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.google.android.gms.maps.model.LatLng
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface MapApiEndPoint {
    @GET("maps/api/place/nearbysearch/json?radius=20000")
    suspend fun getShops(
        @Query("type") type: String = "book_store",
        @Query("key") key: String = GOOGLE_MAPS_API_KEY,
        @Query("location") location: String
    ): Response<MapsResponse?>
}