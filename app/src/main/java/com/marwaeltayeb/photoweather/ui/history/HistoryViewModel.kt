package com.marwaeltayeb.photoweather.ui.history

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class HistoryViewModel : ViewModel() {

    private var historyRepository: HistoryRepository = HistoryRepository()

    fun loadCachedPhotos(context: Context, cacheDir: File) {
        historyRepository.loadCachedPhotos(context, cacheDir)
    }

    fun getCachedPhotos(): MutableLiveData<List<Uri>> {
        return historyRepository.getCachedPhotosLiveData()
    }
}