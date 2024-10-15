package com.shinlee.repository.di

import com.shinlee.repository.AuthenticationRepository
import com.shinlee.repository.AuthenticationRepositoryImp
import com.shinlee.repository.VideoRepository
import com.shinlee.repository.VideoRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    single<VideoRepository> { VideoRepositoryImp(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImp(get()) }
}
