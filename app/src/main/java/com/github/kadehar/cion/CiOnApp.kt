package com.github.kadehar.cion

import android.app.Application
import com.github.kadehar.cion.di.appModule
import com.github.kadehar.cion.di.navModule
import com.github.kadehar.cion.di.videoPlayerModule
import com.github.kadehar.cion.feature.movies_screen.di.moviesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CiOnApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@CiOnApp)
            modules(appModule, navModule, moviesModule, videoPlayerModule)
        }
    }
}