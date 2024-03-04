package com.example.unscramble

import android.app.Application
import com.example.unscramble.data.AppContainer
import com.example.unscramble.data.DefaultAppContainer

class UnScrambleApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}