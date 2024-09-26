package com.shinlee.repository.di

import com.shinlee.repository.MarvelRepository
import com.shinlee.repository.MarvelRepositoryImpl
import com.shinlee.repository.VideoRepository
import com.shinlee.repository.VideoRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    single<MarvelRepository> { MarvelRepositoryImpl(get()) }
    single<VideoRepository> { VideoRepositoryImp(get()) }
}
