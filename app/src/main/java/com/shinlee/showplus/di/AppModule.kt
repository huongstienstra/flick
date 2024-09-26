package com.shinlee.showplus.di


import com.shinlee.showplus.MarvelViewModel
import com.shinlee.showplus.ui.screens.authentication.datasource.SharedPreferencesDataSource
import com.shinlee.showplus.ui.screens.authentication.datasource.SharedPreferencesDataSourceImplement
import com.shinlee.showplus.ui.screens.permission.PermissionViewModel
import com.shinlee.showplus.ui.screens.show.ShowViewModel
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import com.shinlee.showplus.ui.screens.authentication.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
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


    // Provide ShowViewModel and inject PlayersPool with a maxPoolSize dynamically
    viewModel { (maxPoolSize: Int) ->
        ShowViewModel(
            playersPool = get { parametersOf(maxPoolSize) },
            repository = get()
        )
    }
    viewModel { PermissionViewModel(get()) }
    viewModel {
        LoginViewModel(get(), get())
    }
}