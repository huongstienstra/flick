package com.flick.app

import android.app.Application
import com.flick.network.di.networkModule
import com.flick.repository.di.repositoryModule
import com.flick.app.di.appModule
import com.flick.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FlickApplication : Application() {
    companion object {
        private lateinit var privateInstance: FlickApplication
        val instance: FlickApplication
            get() = privateInstance
    }

    override fun onCreate() {
        super.onCreate()
        privateInstance = this

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@FlickApplication)
            modules(listOf(appModule, viewModelModule, networkModule, repositoryModule))
        }
    }
}
