package com.flick.network.di

import com.flick.local.pref.SharedPreferencesDataSource
import com.flick.network.BuildConfig
import com.flick.network.api.PexelsApiService
import com.flick.network.api.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

// Named qualifiers for different API clients
const val AUTH_CLIENT = "auth_client"
const val PEXELS_CLIENT = "pexels_client"
const val AUTH_RETROFIT = "auth_retrofit"
const val PEXELS_RETROFIT = "pexels_retrofit"

val networkModule = module {

    // Auth API (for login/registration)
    single(named(AUTH_CLIENT)) { createAuthOkHttpClient(get()) }
    single(named(AUTH_RETROFIT)) { createAuthRetrofit(get(named(AUTH_CLIENT))) }
    single { createAuthService(get(named(AUTH_RETROFIT))) }

    // Pexels API
    single(named(PEXELS_CLIENT)) { createPexelsOkHttpClient() }
    single(named(PEXELS_RETROFIT)) { createPexelsRetrofit(get(named(PEXELS_CLIENT))) }
    single { createPexelsService(get(named(PEXELS_RETROFIT))) }
}

// Auth API Configuration
fun createAuthOkHttpClient(sharedPreferencesDataSource: SharedPreferencesDataSource): OkHttpClient {
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

fun createAuthRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createAuthService(retrofit: Retrofit): AuthApiService {
    return retrofit.create(AuthApiService::class.java)
}

// Pexels API Configuration
fun createPexelsOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", BuildConfig.PEXELS_API_KEY)
            chain.proceed(requestBuilder.build())
        }
        .build()
}

fun createPexelsRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.PEXELS_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createPexelsService(retrofit: Retrofit): PexelsApiService {
    return retrofit.create(PexelsApiService::class.java)
}
