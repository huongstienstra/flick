package com.flick.app.di


import android.content.Context
import com.google.gson.Gson
import com.flick.local.pref.SharedPreferencesDataSourceImpl
import com.flick.local.pref.SharedPreferencesDataSource
import com.flick.app.FlickApplication
import com.flick.app.ui.screens.show.core.video.ExoPlayerCache
import com.flick.app.ui.screens.show.core.video.NetworkMonitor
import com.flick.app.ui.screens.show.core.video.PlayersPool
import com.flick.app.ui.screens.show.core.video.VideoPreloader
import org.koin.dsl.module

val appModule = module {
    // Network monitor for adaptive buffering
    single { NetworkMonitor(get()) }

    // ExoPlayer cache with adaptive buffering
    single { ExoPlayerCache(get(), get()) }

    // Video preloader for instant next-video playback
    single { VideoPreloader(get()) }

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