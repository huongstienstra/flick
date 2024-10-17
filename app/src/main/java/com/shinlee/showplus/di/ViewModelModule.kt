package com.shinlee.showplus.di

import com.shinlee.showplus.ui.MainViewModel
import com.shinlee.showplus.ui.screens.authentication.signup.LoginViewModel
import com.shinlee.showplus.ui.screens.authentication.signup.phone_number.PhoneNumberViewModel
import com.shinlee.showplus.ui.screens.permission.PermissionViewModel
import com.shinlee.showplus.ui.screens.show.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ShowViewModel(get(), get()) }
    viewModel { PermissionViewModel() }
    viewModel { PhoneNumberViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}