package com.droidblossom.archive.di

import com.droidblossom.archive.data.source.remote.api.SMSMessageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun providesSMSMessageService(retrofit: Retrofit) : SMSMessageService = retrofit.create(SMSMessageService::class.java)
}