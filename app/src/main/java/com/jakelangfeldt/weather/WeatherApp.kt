package com.jakelangfeldt.weather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Weather : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
