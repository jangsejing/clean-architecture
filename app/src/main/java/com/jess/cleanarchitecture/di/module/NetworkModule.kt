package com.jess.cleanarchitecture.di.module

import com.jess.cleanarchitecture.NaverService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author jess
 * @since 2020.06.12
 */
@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    companion object {
        const val NETWORK_TIME_OUT: Long = 5
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("X-Naver-Client-Id", "ZWBj44AXXZBhGsMarS_d")
                header("X-Naver-Client-Secret", "vUUpGBQpS2")
            }.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun createClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            addInterceptor(interceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideNaverService(
        okHttpClient: OkHttpClient
    ): NaverService {
        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverService::class.java)
    }
}