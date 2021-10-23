package com.example.hearthstone.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HearthStoneResponse(
    @SerializedName("classes")
    val classes: List<String>
): Parcelable