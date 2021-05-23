package com.marwaeltayeb.photoweather.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.marwaeltayeb.photoweather.data.local.ImageStorage
import com.marwaeltayeb.photoweather.data.remote.RetrofitClient
import com.marwaeltayeb.photoweather.data.model.CurrentResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class MainRepository {

    fun requestWeatherStatesLiveData(latitude: Double, longitude: Double): Single<CurrentResponse> {
        return RetrofitClient.getWeatherService().getWeatherData(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveImage(context: Context, cacheDir: File, image: Bitmap): Single<Uri> {
        return ImageStorage.saveImage(context, cacheDir, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}