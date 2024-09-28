package com.shinlee.showplus.di


import com.shinlee.showplus.MarvelViewModel
import com.shinlee.showplus.ui.screens.authentication.datasource.SharedPreferencesDataSource
import com.shinlee.showplus.ui.screens.authentication.datasource.SharedPreferencesDataSourceImplement
import com.shinlee.showplus.ui.screens.permission.PermissionViewModel
import com.shinlee.showplus.ui.screens.show.v1.ShowViewModel
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import com.shinlee.showplus.ui.screens.authentication.login.LoginViewModel
import com.shinlee.showplus.ui.screens.show.ShowViewModelV2
import com.shinlee.showplus.ui.screens.show.core.video.ExoPlayerCache
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
    single { ExoPlayerCache(get()) }
    viewModel {
        MarvelViewModel(get(), get())
    }
    // Provide PlayersPool with dynamic maxPoolSize
    factory { (maxPoolSize: Int) ->
        PlayersPool(
            context = get(),
            maxPoolSize = maxPoolSize
        )
    }

    // provide Share preference
    single<SharedPreferencesDataSource> { SharedPreferencesDataSourceImplement() }

    viewModel { (maxPoolSize: Int) ->
        ShowViewModel(
            playersPool = get { parametersOf(maxPoolSize) },
            repository = get()
        )
    }
    viewModel { ShowViewModelV2(get(), get()) }
    viewModel { PermissionViewModel(get()) }
    viewModel {
        LoginViewModel(get(), get())
    }
}