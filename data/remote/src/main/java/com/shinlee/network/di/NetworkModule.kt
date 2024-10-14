package com.shinlee.network.di

import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.network.BuildConfig
import com.shinlee.network.api.ShowPlusApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

val networkModule = module {

    single { createService(get()) }

    single { createRetrofit(get()) }

    single { createOkHttpClient(get()) }

}

fun createOkHttpClient(sharedPreferencesDataSource: SharedPreferencesDataSource): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val token = sharedPreferencesDataSource.getToken()
            requestBuilder.addHeader("Authorization", "Bearer $token")
            chain.proceed(requestBuilder.build())
        }
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createService(retrofit: Retrofit): ShowPlusApiService {
    return retrofit.create(ShowPlusApiService::class.java)
}



