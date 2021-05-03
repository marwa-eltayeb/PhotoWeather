package com.marwaeltayeb.photoweather.data.model

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    val temp: Double,
)