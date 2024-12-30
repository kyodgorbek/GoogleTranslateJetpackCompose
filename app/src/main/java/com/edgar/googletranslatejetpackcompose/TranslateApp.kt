package com.edgar.googletranslatejetpackcompose

import android.app.Application
import com.edgar.googletranslatejetpackcompose.di.mapperModule
import com.edgar.googletranslatejetpackcompose.di.networkModule
import com.edgar.googletranslatejetpackcompose.di.repositoryModule
import com.edgar.googletranslatejetpackcompose.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TranslateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TranslateApp)
            modules(networkModule, mapperModule, repositoryModule, viewModelModule)
        }
    }
}