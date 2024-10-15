package com.shinlee.showplus.di

import com.shinlee.showplus.ui.MainViewModel
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginViewModelV2
import com.shinlee.showplus.ui.screens.authentication.signup.LoginViewModel
import com.shinlee.showplus.ui.screens.permission.PermissionViewModel
import com.shinlee.showplus.ui.screens.show.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ShowViewModel(get(), get()) }
    viewModel { PermissionViewModel() }
    viewModel { LoginViewModelV2(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}