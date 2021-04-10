package com.marwaeltayeb.photoweather.data.remote

import com.marwaeltayeb.photoweather.data.model.CurrentResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("weather")
    fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unitName: String,
        @Query("appid") apiKey: String
    ): Single<CurrentResponse>
}

