package com.droidblossom.archive.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesConverterFactory() : GsonConverterFactory{
        return GsonConverterFactory.create(
            GsonBuilder()
                .create()
        )
    }

    @Singleton
    @Provides
    fun providesOkHttpClient() : OkHttpClient.Builder{
        return OkHttpClient.Builder().apply {
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }

    //TODO baseurl ignore 시켜야함
    /*
    @Provides
    @Singleton
    fun providesRetrofit(
        client : OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(gsonConverterFactory)
            .client(client.build())
            .build()
    }
    */
}