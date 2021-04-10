package com.marwaeltayeb.photoweather.data.remote

import com.marwaeltayeb.photoweather.utils.Const.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun getWeatherService(): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}

