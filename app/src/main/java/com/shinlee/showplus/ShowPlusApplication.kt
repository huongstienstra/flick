package com.shinlee.showplus

import android.app.Application
import com.shinlee.network.di.networkModule
import com.shinlee.repository.di.repositoryModule
import com.shinlee.showplus.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShowPlusApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ShowPlusApplication)
            modules(listOf(appModule, networkModule, repositoryModule))
        }
    }
}