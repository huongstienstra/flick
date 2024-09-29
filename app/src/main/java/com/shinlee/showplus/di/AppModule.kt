package com.shinlee.showplus.di


import android.content.Context
import com.shinlee.local.pref.SharedPreferencesDataSourceImpl
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.showplus.ShowPlusApplication
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import com.shinlee.showplus.ui.screens.show.core.video.ExoPlayerCache
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
        ShowPlusApplication.instance.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    single<SharedPreferencesDataSource> {
        SharedPreferencesDataSourceImpl(
            sharedPreferences = get()
        )
    }

}