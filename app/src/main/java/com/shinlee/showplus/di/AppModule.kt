package com.shinlee.showplus.di

import com.shinlee.showplus.MarvelViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MarvelViewModel(get())
    }
}