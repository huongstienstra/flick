package com.shinlee.showplus.di

import com.shinlee.showplus.MarvelViewModel
import com.shinlee.showplus.ui.screens.permission.PermissionViewModel
import com.shinlee.showplus.ui.screens.show.ShowViewModel
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MarvelViewModel(get())
    }
    // Provide PlayersPool with dynamic maxPoolSize
    factory { (maxPoolSize: Int) ->
        PlayersPool(
            context = get(),
            maxPoolSize = maxPoolSize
        )
    }

    // Provide ShowViewModel and inject PlayersPool with a maxPoolSize dynamically
    viewModel { (maxPoolSize: Int) ->
        ShowViewModel(
            playersPool = get { parametersOf(maxPoolSize) }
        )
    }
    viewModel { PermissionViewModel(get()) }
}