package com.marwaeltayeb.photoweather.di

import com.marwaeltayeb.photoweather.data.remote.WeatherService
import com.marwaeltayeb.photoweather.ui.history.HistoryAdapter
import com.marwaeltayeb.photoweather.ui.history.HistoryRepository
import com.marwaeltayeb.photoweather.ui.history.HistoryViewModel
import com.marwaeltayeb.photoweather.ui.main.MainRepository
import com.marwaeltayeb.photoweather.ui.main.MainViewModel
import com.marwaeltayeb.photoweather.utils.Const
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}

val repositoryModule = module {
    single { MainRepository(get()) }
    single { HistoryRepository() }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}
