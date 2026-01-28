package com.flick.repository.di

import com.flick.local.VideoInteractionStorage
import com.flick.local.VideoInteractionStorageImpl
import com.flick.repository.AuthenticationRepository
import com.flick.repository.AuthenticationRepositoryImp
import com.flick.repository.VideoRepository
import com.flick.repository.VideoRepositoryImp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<VideoInteractionStorage> { VideoInteractionStorageImpl(androidContext()) }
    single<VideoRepository> { VideoRepositoryImp(get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImp(get()) }
}
