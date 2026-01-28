package com.flick.app.di


import android.content.Context
import com.google.gson.Gson
import com.flick.local.pref.SharedPreferencesDataSourceImpl
import com.flick.local.pref.SharedPreferencesDataSource
import com.flick.app.FlickApplication
import com.flick.app.ui.screens.show.core.video.PlayersPool
import com.flick.app.ui.screens.show.core.video.ExoPlayerCache
import org.koin.dsl.module

val appModule = module {
    single { ExoPlayerCache(get()) }

    factory { (maxPoolSize: Int) ->
        PlayersPool(
            context = get(),
            maxPoolSize = maxPoolSize
        )
    }

    single {
        FlickApplication.instance.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    single<SharedPreferencesDataSource> {
        SharedPreferencesDataSourceImpl(
            sharedPreferences = get()
        )
    }

}