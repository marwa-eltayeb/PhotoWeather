package com.marwaeltayeb.photoweather.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.photoweather.data.model.CurrentResponse
import java.io.File

class MainViewModel : ViewModel() {

    private var mainRepository: MainRepository = MainRepository()

    fun requestWeatherStates(latitude: Double, longitude : Double, unitName: String , apiKey: String) {
        mainRepository.requestWeatherStatesLiveData(latitude, longitude, unitName, apiKey)
    }

    fun getWeatherStates(): LiveData<CurrentResponse>{
        return mainRepository.getWeatherStatesLiveData()
    }

    fun saveImage(context: Context, cacheDir: File, image: Bitmap) {
        mainRepository.saveImage(context, cacheDir, image)
    }

    fun getPhotoUri(): LiveData<Uri>{
        return mainRepository.getPhotoUriLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        mainRepository.clearCompositeDisposable()
    }
}

