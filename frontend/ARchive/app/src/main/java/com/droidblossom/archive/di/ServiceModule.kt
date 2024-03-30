package com.droidblossom.archive.di

import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.CapsuleService
import com.droidblossom.archive.data.source.remote.api.CapsuleSkinService
import com.droidblossom.archive.data.source.remote.api.FriendService
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.data.source.remote.api.S3Service
import com.droidblossom.archive.data.source.remote.api.SecretService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun providesAuthService(retrofit: Retrofit) : AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesMemberService(retrofit: Retrofit) : MemberService = retrofit.create(MemberService::class.java)

    @Singleton
    @Provides
    fun providesSecretService(retrofit: Retrofit) : SecretService = retrofit.create(SecretService::class.java)

    @Singleton
    @Provides
    fun providesS3Service(retrofit: Retrofit) : S3Service = retrofit.create(S3Service::class.java)

    @Singleton
    @Provides
    fun providesCapsuleService(retrofit: Retrofit) : CapsuleService = retrofit.create(CapsuleService::class.java)

    @Singleton
    @Provides
    fun providesCapsuleSkinService(retrofit: Retrofit) : CapsuleSkinService = retrofit.create(CapsuleSkinService::class.java)

    @Singleton
    @Provides
    fun providesFriendService(retrofit : Retrofit) : FriendService = retrofit.create(FriendService::class.java)
}