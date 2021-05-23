package com.marwaeltayeb.photoweather.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.photoweather.data.model.CurrentResponse
import io.reactivex.disposables.CompositeDisposable
import java.io.File

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private var mainRepository: MainRepository = MainRepository()
    private val weatherStateLiveData: MutableLiveData<CurrentResponse> = MutableLiveData<CurrentResponse>()
    private val photoUriLiveData: MutableLiveData<Uri> = MutableLiveData<Uri>()
    private var compositeDisposable = CompositeDisposable()

    fun requestWeatherStates(latitude: Double, longitude: Double) {
        val observable = mainRepository.requestWeatherStatesLiveData(latitude, longitude)
            .subscribe({ response: CurrentResponse? ->
                Log.d(TAG, "Succeeded")
                weatherStateLiveData.setValue(response)
            }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getWeatherStates(): LiveData<CurrentResponse> {
        return weatherStateLiveData
    }

    fun saveImage(context: Context, cacheDir: File, image: Bitmap) {
        val observable = mainRepository.saveImage(context, cacheDir, image)
            .subscribe({ photoUri: Uri? ->
                Log.d(TAG, "Saved")
                photoUriLiveData.setValue(photoUri)
            }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getPhotoUri(): LiveData<Uri> {
        return photoUriLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

