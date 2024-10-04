package com.shinlee.showplus.di

import com.shinlee.showplus.ShareViewModel
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginViewModelV2
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel
import com.shinlee.showplus.ui.screens.authentication.term.TermViewModel
import com.shinlee.showplus.ui.screens.permission.PermissionViewModel
import com.shinlee.showplus.ui.screens.show.ShowViewModelV2
import com.shinlee.showplus.ui.screens.show.v1.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (maxPoolSize: Int) ->
        ShowViewModel(
            playersPool = get { parametersOf(maxPoolSize) },
            repository = get()
        )
    }
    viewModel { ShowViewModelV2(get(), get()) }
    viewModel { PermissionViewModel(get()) }
    viewModel { LoginViewModelV2(get(), get()) }
    viewModel { ShareViewModel(get()) }
    viewModel { TermViewModel() }
    viewModel { SignupViewModel(get(), get()) }
}