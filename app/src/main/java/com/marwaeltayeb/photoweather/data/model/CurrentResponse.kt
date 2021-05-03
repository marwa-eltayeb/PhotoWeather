package com.marwaeltayeb.photoweather.data.model

import com.google.gson.annotations.SerializedName

data class CurrentResponse(

    @SerializedName("weather")
    val weather: List<Weather>,

    @SerializedName("main")
    val main: Main,

    @SerializedName("name")
    val cityName: String,
)



