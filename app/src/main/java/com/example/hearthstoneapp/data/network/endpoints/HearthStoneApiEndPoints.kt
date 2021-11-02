package com.example.hearthstone.data.network.endpoints

import com.example.hearthstone.data.network.model.HearthStoneResponse
import com.example.hearthstoneapp.data.network.model.SearchResponse
import com.example.hearthstoneapp.util.secret.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HearthStoneApiEndPoints {
    @GET("info")
    suspend fun getInfo(
        @Header("x-rapidapi-host") host: String = "omgvamp-hearthstone-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") key: String = API_KEY
    ): Response<HearthStoneResponse>

    @GET("cards/search/{name}")
    suspend fun searchCards(
        @Header("x-rapidapi-host") host: String = "omgvamp-hearthstone-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") key: String = API_KEY,
        @Path("name") name: String
    ): Response<List<SearchResponse>>

    @GET("cards/classes/{cardClass}")
    suspend fun searchCardsbyClass(
        @Header("x-rapidapi-host") host: String = "omgvamp-hearthstone-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") key: String = API_KEY,
        @Path("cardClass") cardClass: String
    ): Response<List<SearchResponse>>
}