package com.marwaeltayeb.photoweather.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.marwaeltayeb.photoweather.BuildConfig
import io.reactivex.Single
import java.io.File

private const val TAG = "FileUtils"

object FileUtils {

    fun getPhotoFiles(context: Context, cacheDir: File) : Single<ArrayList<Uri>> {
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
        }
        return Single.just(historyList)
    }
}