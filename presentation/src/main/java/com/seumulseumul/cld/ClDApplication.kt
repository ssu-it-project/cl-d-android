package com.seumulseumul.cld

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClDApplication: Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var instance: ClDApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }
}