package com.marwaeltayeb.photoweather.ui.history

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import java.io.File

private const val TAG = "HistoryViewModel"

class HistoryViewModel : ViewModel() {

    private var historyRepository: HistoryRepository = HistoryRepository()
    private val imagesPhotoUris: MutableLiveData<List<Uri>> = MutableLiveData<List<Uri>>()
    private var compositeDisposable = CompositeDisposable()

    fun loadCachedPhotos(context: Context, cacheDir: File) {
        val observable = historyRepository.loadCachedPhotos(context, cacheDir)
            .subscribe({ historyList: ArrayList<Uri> ->
            Log.d(TAG, "Loaded")
            imagesPhotoUris.value = historyList },
            { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getCachedPhotos(): MutableLiveData<List<Uri>> {
        return imagesPhotoUris
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}