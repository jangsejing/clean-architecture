package com.jess.cleanarchitecture

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JessApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}