package com.droidblossom.archive.di

import com.droidblossom.archive.BuildConfig
import com.droidblossom.archive.util.AccessTokenInterceptor
import com.droidblossom.archive.util.TokenAuthenticator
import com.droidblossom.archive.util.TokenUtils
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ResourceRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ResourceRetrofitWithToken

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
        pf: TokenUtils
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            authenticator(TokenAuthenticator(pf))
            addNetworkInterceptor(AccessTokenInterceptor(pf))
        }.build()
    }
//
//    @Provides
//    @Singleton
//    fun providesOkHttpClientWithToken(sharedPreferences: SharedPreferences) : OkHttpClient {
//        val token = sharedPreferences.getString("token_key", "") ?: ""
//        return OkHttpClient.Builder().apply {
//            if (token.isNotEmpty()) {
//                addInterceptor { chain ->
//                    val newRequest = chain.request().newBuilder()
//                        .addHeader("Authorization", "Bearer $token")
//                        .build()
//                    chain.proceed(newRequest)
//                }
//            }
//            connectTimeout(5, TimeUnit.SECONDS)
//            readTimeout(5, TimeUnit.SECONDS)
//            writeTimeout(5, TimeUnit.SECONDS)
//            addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//        }.build()
//    }


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

//    @Provides
//    @Singleton
//    @ResourceRetrofitWithToken
//    fun providesRetrofitWithToken(
//        client : OkHttpClient,
//        gsonConverterFactory: GsonConverterFactory
//    ) : Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(gsonConverterFactory)
//            .client(client)
//            .build()
//    }

}