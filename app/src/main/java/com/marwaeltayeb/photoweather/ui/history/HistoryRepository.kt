package com.marwaeltayeb.photoweather.ui.history

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.photoweather.BuildConfig
import java.io.File

private const val TAG = "HistoryRepository"

class HistoryRepository {

    private val imagesPhotoUris: MutableLiveData<List<Uri>> = MutableLiveData<List<Uri>>()

    fun loadCachedPhotos(context: Context, cacheDir: File) {
        val imagesFolder = File(cacheDir, "images")
        val lists = imagesFolder.listFiles()
        val historyList: ArrayList<Uri> = ArrayList()
        if (lists != null) {
            for (item in lists) {
                Log.d(TAG, item.absolutePath + "")
                val uri: Uri = FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    item
                )
                historyList.add(uri)
            }
            imagesPhotoUris.value = historyList
        }
    }

    fun getCachedPhotosLiveData(): MutableLiveData<List<Uri>> {
        return imagesPhotoUris
    }
}