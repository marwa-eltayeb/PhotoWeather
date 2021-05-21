package com.marwaeltayeb.photoweather.data.remote

import com.marwaeltayeb.photoweather.data.model.CurrentResponse
import com.marwaeltayeb.photoweather.utils.Const
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("weather")
    fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unitName: String = Const.UNIT_NAME,
        @Query("appid") apiKey: String = Const.API_KEY
    ): Single<CurrentResponse>
}

