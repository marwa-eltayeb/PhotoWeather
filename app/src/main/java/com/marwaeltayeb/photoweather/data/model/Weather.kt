package com.marwaeltayeb.photoweather.data.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("main")
    val weatherState: String,
)