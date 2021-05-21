package com.marwaeltayeb.photoweather.ui.history

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.photoweather.BuildConfig
import com.marwaeltayeb.photoweather.data.local.ImageStorage
import com.marwaeltayeb.photoweather.utils.FileUtils.getPhotoFiles
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

private const val TAG = "HistoryRepository"

class HistoryRepository {

    private val imagesPhotoUris: MutableLiveData<List<Uri>> = MutableLiveData<List<Uri>>()

    var compositeDisposable = CompositeDisposable()

    fun loadCachedPhotos(context: Context, cacheDir: File) {
        val observable = getPhotoFiles(context, cacheDir)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ historyList: ArrayList<Uri> ->
                Log.d(TAG, "Loaded")
                imagesPhotoUris.value = historyList },
                { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getCachedPhotosLiveData(): MutableLiveData<List<Uri>> {
        return imagesPhotoUris
    }

    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
}