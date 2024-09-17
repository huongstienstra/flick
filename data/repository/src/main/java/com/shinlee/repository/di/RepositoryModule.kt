package com.shinlee.repository.di

import com.shinlee.repository.MarvelRepository
import com.shinlee.repository.MarvelRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MarvelRepository> { MarvelRepositoryImpl(get()) }
}