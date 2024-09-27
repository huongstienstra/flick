package com.shinlee.showplus

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.shinlee.network.di.networkModule
import com.shinlee.repository.di.repositoryModule
import com.shinlee.showplus.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ShowPlusApplication : Application() {

    companion object {
        private lateinit var privateInstance: ShowPlusApplication
        val instance: ShowPlusApplication
            get() = privateInstance
    }

    val sharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(this)
    override fun onCreate() {
        super.onCreate()
        privateInstance = this
        startKoin {
            androidContext(this@ShowPlusApplication)
            modules(listOf(appModule, networkModule, repositoryModule))
        }
    }
}