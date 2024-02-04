package com.example.shoppie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ShoppieApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant()
    }
}