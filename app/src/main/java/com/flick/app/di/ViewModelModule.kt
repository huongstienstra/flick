package com.flick.app.di

import com.flick.app.ui.MainViewModel
import com.flick.app.ui.screens.authentication.signup.LoginViewModel
import com.flick.app.ui.screens.authentication.signup.phone_number.PhoneNumberViewModel
import com.flick.app.ui.screens.permission.PermissionViewModel
import com.flick.app.ui.screens.search.SearchViewModel
import com.flick.app.ui.screens.show.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ShowViewModel(get(), get(), get()) }
    viewModel { PermissionViewModel() }
    viewModel { PhoneNumberViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
}
