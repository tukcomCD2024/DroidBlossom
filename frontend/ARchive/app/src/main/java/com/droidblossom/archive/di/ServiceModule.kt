package com.droidblossom.archive.di

import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.MemberService
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

    @Singleton
    @Provides
    fun providesAuthService(retrofit: Retrofit) : AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesMemberService(retrofit: Retrofit) : MemberService = retrofit.create(MemberService::class.java)


}