package com.example.hearthstoneapp.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponse(
    var cardId: String = "",
    var name: String? = null,
    var type: String? = null,
    var rarity: String? = null,
    var cardSet: String? = null,
    @SerializedName("img")
    var img: String? = null,
    @SerializedName("text")
    var effect: String? = null,
    var playerClass: String? = null
) : Parcelable