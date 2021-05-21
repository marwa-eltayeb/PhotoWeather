package com.marwaeltayeb.photoweather.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.photoweather.data.local.ImageStorage
import com.marwaeltayeb.photoweather.data.remote.RetrofitClient
import com.marwaeltayeb.photoweather.data.model.CurrentResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

private const val TAG = "MainRepository"

class MainRepository {

    private val weatherStateLiveData: MutableLiveData<CurrentResponse> = MutableLiveData<CurrentResponse>()
    private val photoUriLiveData: MutableLiveData<Uri> = MutableLiveData<Uri>()


    var compositeDisposable = CompositeDisposable()

    fun requestWeatherStatesLiveData(latitude: Double, longitude: Double) {

        val observable =
            RetrofitClient.getWeatherService().getWeatherData(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: CurrentResponse? ->
                    Log.d(TAG, "Succeeded")
                    weatherStateLiveData.setValue(response)
                }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getWeatherStatesLiveData(): MutableLiveData<CurrentResponse> {
        return weatherStateLiveData
    }

    fun saveImage(context: Context, cacheDir: File, image: Bitmap) {
        val observable = ImageStorage.saveImage(context, cacheDir, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photoUri: Uri? ->
                Log.d(TAG, "Saved")
                photoUriLiveData.setValue(photoUri) },
                { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })


        compositeDisposable.add(observable)
    }


    fun getPhotoUriLiveData(): MutableLiveData<Uri> {
        return photoUriLiveData
    }


    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
}