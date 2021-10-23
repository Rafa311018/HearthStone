package com.example.hearthstoneapp.data.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponse(
    val cardId: String,
    val name: String,
    val type: String,
    val rarity: String,
    val cardSet: String,
    val img: String
): Parcelable