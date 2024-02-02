package com.droidblossom.archive.di

import android.content.Context
import com.droidblossom.archive.data.source.remote.api.KakaoService
import com.droidblossom.archive.util.S3Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExternalApiModule {

    @Provides
    @Singleton
    fun providesS3Utils(@ApplicationContext context: Context) : S3Util {
        return S3Util(context)
    }

    @Singleton
    @Provides
    fun providesKakaoService(@Named("kakao") retrofit: Retrofit) : KakaoService = retrofit.create(KakaoService::class.java)

}