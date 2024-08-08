package com.droidblossom.archive.di

import com.droidblossom.archive.BuildConfig
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.util.AccessTokenInterceptor
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.ErrorHandlingInterceptor
import com.droidblossom.archive.util.TokenAuthenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    const val NETWORK_EXCEPTION_OFFLINE = "network_exception_offline"
    const val NETWORK_EXCEPTION_RESULT_IS_NULL = "network_exception_result_is_null"

    @Provides
    @Singleton
    fun providesConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create()
        )
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        ds: DataStoreUtils,
        authServiceLazy: Lazy<AuthService>,
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().apply {
            addInterceptor (interceptor)
            addInterceptor(ErrorHandlingInterceptor())
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            authenticator(TokenAuthenticator(ds, authServiceLazy))
            addNetworkInterceptor(AccessTokenInterceptor(ds))
        }.build()
    }

    @Provides
    @Singleton
    @Named("kakaoClient")
    fun providesKOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().apply {

            addInterceptor (interceptor)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()
    }


    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}api/")
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("kakao")
    fun providesKakaoRetrofit(
        @Named("kakaoClient") client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

}