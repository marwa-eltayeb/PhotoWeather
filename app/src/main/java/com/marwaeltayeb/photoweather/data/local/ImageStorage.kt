package com.marwaeltayeb.photoweather.data.local

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.marwaeltayeb.photoweather.BuildConfig
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val TAG = "ImageStorage"

object ImageStorage {

    fun saveImage(context: Context, cacheDir : File, image: Bitmap): Single<Uri> {
        val imagesFolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "preview" + Math.random() + ".png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                file
            )
        } catch (e: IOException) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.message)
        }
        return Single.just(uri)
    }
}