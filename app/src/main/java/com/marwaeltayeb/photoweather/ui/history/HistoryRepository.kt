package com.marwaeltayeb.photoweather.ui.history

import android.content.Context
import android.net.Uri
import com.marwaeltayeb.photoweather.utils.FileUtils.getPhotoFiles
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class HistoryRepository {

    fun loadCachedPhotos(context: Context, cacheDir: File): Single<ArrayList<Uri>> {
        return getPhotoFiles(context, cacheDir)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

