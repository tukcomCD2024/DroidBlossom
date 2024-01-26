package com.droidblossom.archive.di

import com.droidblossom.archive.BuildConfig
import com.droidblossom.archive.util.AccessTokenInterceptor
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.TokenAuthenticator
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
                .create()
        )
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        ds: DataStoreUtils
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            authenticator(TokenAuthenticator(ds))
            addNetworkInterceptor(AccessTokenInterceptor(ds))
        }.build()
    }


    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

}