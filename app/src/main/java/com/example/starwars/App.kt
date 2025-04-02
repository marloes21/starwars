package com.example.starwars

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StarWarsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any libraries or perform any setup here
    }
}